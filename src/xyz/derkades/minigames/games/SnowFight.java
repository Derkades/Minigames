package xyz.derkades.minigames.games;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.DARK_GRAY;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
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

	@Override
	void begin() {
		dead = new ArrayList<>();
		
		Utils.setGameRule("doTileDrops", false);
		
		for (Player player: Bukkit.getOnlinePlayers()){
			player.teleport(new Location(Var.WORLD, 218.5, 75, 291.5, 90, 0));
			
			Minigames.setCanTakeDamage(player, true);
		}
		
		Console.sendCommand("replaceitem entity @a slot.hotbar.0 minecraft:diamond_shovel 1 0 {display:{Name:\"Snow Shoveler\"},Unbreakable:1,ench:[{id:32,lvl:1}],CanDestroy:[\"minecraft:snow_layer\"]}");
		//timer();
		//sendMessage("Game has started!");
		
		new BukkitRunnable() {
			
			int secondsLeft = MAX_DURATION;
			
			public void run() {
				//End the game if everyone is a spectator except one player
				if (dead.size() == (Bukkit.getOnlinePlayers().size() - 1) && secondsLeft > 2) {
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
			return;
		}
			
		Player player = event.getPlayer();
		Inventory inv = player.getInventory();
		if (!inv.contains(new ItemStack(Material.SNOW_BALL, 16))) {
			int amount = Random.getRandomInteger(1, 3);
			inv.addItem(new ItemStack(Material.SNOW_BALL, amount));
		}

		final Material oldType = block.getType();
		@SuppressWarnings("deprecation")
		final byte oldData = block.getData();

		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				block.setType(oldType);
				block.setData(oldData);
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
				player.teleport(new Location(Var.WORLD, 224.5, 79.1, 291.5, 90, 0));
				dead.add(player.getUniqueId());
				Utils.clearInventory(player);
				Minigames.setCanTakeDamage(player, false);
			});
		}
	}
	
}
