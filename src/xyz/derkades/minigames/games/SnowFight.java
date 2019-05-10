package xyz.derkades.minigames.games;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.DARK_GRAY;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.snowfight.SnowFightMap;
import xyz.derkades.minigames.utils.Console;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class SnowFight extends Game {
	
	private static final int MAX_DURATION = 70;
	
	SnowFight() {
		super("Snow Fight", new String[] {
				"In this game you have to kill other players",
				"using snowballs. Snowballs do 2.5 hearts",
				"damage. Get snowballs by breaking snow",
				"on the ground. Good luck!"
		}, 2, 4, 9);
	}
	
	private List<UUID> dead;
	private SnowFightMap map;
	
	@Override
	void begin() {
		dead = new ArrayList<>();
		map = ListUtils.getRandomValueFromArray(SnowFightMap.MAPS);
		
		Utils.setGameRule("doTileDrops", false);
		
		for (Player player: Bukkit.getOnlinePlayers()){
			player.teleport(map.getSpawnLocation());
			
			Minigames.setCanTakeDamage(player, true);
		}
		
		Console.sendCommand("replaceitem entity @a slot.hotbar.0 minecraft:diamond_shovel 1 0 {display:{Name:\"Snow Shoveler\"},Unbreakable:1,ench:[{id:32,lvl:1}],CanDestroy:[\"minecraft:snow_layer\"]}");
		
		new BukkitRunnable() {
			
			int secondsLeft = MAX_DURATION;
			
			public void run() {
				//End the game if everyone is a spectator except one player (or everyone is a spectator)
				if (dead.size() >= (Bukkit.getOnlinePlayers().size() - 1) && secondsLeft > 2) {
					secondsLeft = 2;
				}
				
				if (secondsLeft <= 0) {
					this.cancel();
					endGame();
					return;
				}
				
				if (secondsLeft == 30 || secondsLeft == 15 || secondsLeft <= 5) {
					sendMessage(String.format("%s seconds left", secondsLeft));
				}
				
				secondsLeft--;
			}
			
		}.runTaskTimer(Minigames.getInstance(), 0, 1*20);
	}
	
	private void endGame(){
		Utils.setGameRule("doTileDrops", true);
		
		for (Player player: Bukkit.getOnlinePlayers()){
			player.setHealth(20);
		}
		
		super.startNextGame(Utils.getWinnersFromDeadList(dead));
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
		if (event.getDamager() instanceof Snowball){
			event.setDamage(4);
		} else {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		Block block = event.getBlock();
		if (block.getType() != Material.SNOW){
			event.setCancelled(true);
			return;
		}
			
		Player player = event.getPlayer();
		Inventory inv = player.getInventory();
		if (!inv.contains(new ItemStack(Material.SNOWBALL, 16))) {
			int amount = Random.getRandomInteger(1, 3);
			inv.addItem(new ItemStack(Material.SNOWBALL, amount));
		}
		
		Snow snow = (Snow) block.getBlockData();

		int oldLayersNum = snow.getLayers();

		new BukkitRunnable() {
			public void run() {
				snow.setLayers(oldLayersNum);
			}
		}.runTaskLater(Minigames.getInstance(), 3 * 20);
	}
	
	@EventHandler
	public void onKill(PlayerDeathEvent event){
		Player killer = event.getEntity().getKiller();
		
		if (event.getEntityType() == EntityType.PLAYER){
			Player player = event.getEntity().getPlayer();
			String pn = player.getName();
			event.setDeathMessage(DARK_GRAY + "[" + DARK_AQUA + getName() + DARK_GRAY + "] " + AQUA + killer.getName() + " has killed " + pn + "!");
			Scheduler.delay(1, () -> {
				player.spigot().respawn();
				player.teleport(map.getSpectatorLocation());
				dead.add(player.getUniqueId());
				Utils.clearInventory(player);
				Minigames.setCanTakeDamage(player, false);
			});
		}
	}
	
}
