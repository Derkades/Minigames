package xyz.derkades.minigames.games;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.DARK_GRAY;

import java.util.HashMap;
import java.util.Map;

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

import xyz.derkades.minigames.Main;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.Console;
import xyz.derkades.minigames.utils.Utils;
import xyz.derkades.minigames.utils.java.Random;

public class SnowFight extends Game {

	private Map<String, Boolean> isDead = new HashMap<>();
	
	@Override
	String[] getDescription() {
		return new String[]{
				"In this game you have to kill other players",
				"using snowballs. Snowballs do 2.5 hearts",
				"damage. Get snowballs by breaking snow",
				"on the ground. Good luck!"
				};
	}

	@Override
	public String getName() {
		return "Snow Fight";
	}

	@Override
	public int getRequiredPlayers() {
		return 2;
	}

	@Override
	public GamePoints getPoints() {
		return new GamePoints(4, 9);
	}

	@Override
	public void resetHashMaps(Player player) {
		isDead.put(player.getName(), false);
	}

	@Override
	void begin() {
		Utils.setGameRule("doTileDrops", false);
		
		for (Player player: Bukkit.getOnlinePlayers()){
			player.teleport(new Location(Var.WORLD, 212.5, 75, 291.5, 0, 90));
			
			//Main.CAN_TAKE_DAMAGE.put(player.getName(), true);
			Utils.setCanTakeDamage(player, true);
		}
		
		Console.sendCommand("replaceitem entity @a slot.hotbar.0 minecraft:diamond_shovel 1 0 {display:{Name:\"Snow Shoveler\"},Unbreakable:1,ench:[{id:32,lvl:1}],CanDestroy:[\"minecraft:snow_layer\"]}");
		timer();
		sendMessage("Game has started!");
	}
	
	private void timer(){
		new BukkitRunnable(){
			public void run(){
				sendMessage("5 seconds left!");
				new BukkitRunnable(){
					public void run(){
						endGame();
					}
				}.runTaskLater(Main.getInstance(), 5*20);
			}
		}.runTaskLater(Main.getInstance(), 25*20);
	}
	
	private void endGame(){
		Utils.setGameRule("doTileDrops", true);
		
		for (Player player: Bukkit.getOnlinePlayers()){
			player.setHealth(20);
		}
		super.startNextGame(Utils.getWinnersFromIsDeadHashMap(isDead));
	}

	private void playerDie(Player player){
		player.teleport(new Location(Var.WORLD, 224.5, 79.1, 291.5, 90, 0));
		isDead.put(player.getName(), true);
		Utils.clearInventory(player);
		Utils.setCanTakeDamage(player, false);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
		if (!isRunning()) return;
		
		if (event.getDamager() instanceof Snowball){
			event.setDamage(4);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if (!isRunning()) return;
		
		Block block = event.getBlock();
		if (block.getType() == Material.SNOW){
			Player player = event.getPlayer();
			Inventory inv = player.getInventory();
			if (!inv.contains(new ItemStack(Material.SNOW_BALL, 16))){
				int amount = Random.getRandomInteger(1, 3);
				inv.addItem(new ItemStack(Material.SNOW_BALL, amount));
			}
			
			final Material oldType = block.getType();
			@SuppressWarnings("deprecation")
			final byte oldData = block.getData();
			
			new BukkitRunnable(){
				@SuppressWarnings("deprecation")
				public void run(){
					block.setType(oldType);
					block.setData(oldData);
				}
			}.runTaskLater(Main.getInstance(), 3*20);
			
		}
	}
	
	@EventHandler
	public void onKill(PlayerDeathEvent event){
		if (!isRunning()) return;
		
		Player killer = event.getEntity().getKiller();
		
		if (event.getEntityType() == EntityType.PLAYER){
			Player player = event.getEntity().getPlayer();
			String pn = player.getName();
			event.setDeathMessage(DARK_GRAY + "[" + DARK_AQUA + getName() + DARK_GRAY + "] " + AQUA + killer.getName() + " has killed " + pn + "!");
			new BukkitRunnable(){
				public void run(){
					player.spigot().respawn();
					playerDie(player);
				}
			}.runTaskLater(Main.getInstance(), 5L);
			playerDie(player);
		}
	}
	
}
