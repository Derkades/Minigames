package xyz.derkades.minigames.games;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.Main;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.Utils;

public class Speedrun extends Game {

	private Map<String, Boolean> hasFinished = new HashMap<>();
	
	private boolean NO_ONE_FINISHED = true;
	
	@Override
	String[] getDescription() {
		return new String[]{
				"Insert description here"
				};
	}

	@Override
	public String getName() {
		return "Speedrun";
	}

	@Override
	public int getRequiredPlayers() {
		return 1;
	}

	@Override
	public GamePoints getPoints() {
		return new GamePoints(2, 4);
	}

	@Override
	public void resetHashMaps(Player player) {
		hasFinished.put(player.getName(), false);
	}

	@Override
	void begin(){
		this.NO_ONE_FINISHED = true;
		for (Player player : Bukkit.getOnlinePlayers()){
			player.teleport(new Location(Var.WORLD, 140.0, 97, 306, -180, 0));
			Utils.giveInfiniteEffect(player, PotionEffectType.SPEED, 30);
		}
		
		timer();
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
		super.startNextGame(Utils.getWinnersFromFinishedHashMap(hasFinished));
	}
	
	private void playerDie(Player player){
		player.teleport(new Location(Var.WORLD, 140.0, 97, 306, -180, 0));
	}
	
	private void playerWin(Player player){
		Utils.clearPotionEffects(player);
		if (NO_ONE_FINISHED){
			NO_ONE_FINISHED = false;
			super.sendMessage(player.getName() + " finished first and got an extra point!");
			Points.addPoints(player, 1);
			hasFinished.put(player.getName(), true);
		} else {
			super.sendMessage(player.getName() + " has finished!");
			hasFinished.put(player.getName(), true);
		}
		player.teleport(new Location(Var.WORLD, 128.0, 98, 274.5, -180, 0));
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(PlayerMoveEvent event){
		if (!isRunning()) return;

		Player player = event.getPlayer();
		
		if (hasFinished.get(player.getName())){
			return;
		}
		
		if (player.isSneaking()){
			player.sendMessage(ChatColor.RED + "You cannot sneak!");
			playerDie(player);
			return;
		}
		
		if (player.isSprinting()){
			player.sendMessage(ChatColor.RED + "You cannot sprint!");
			playerDie(player);
			return;
		}
		
		Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
		Material type = block.getType();
		if (type == Material.STAINED_CLAY && block.getData() == 14){
			playerDie(player);
		} else if(type == Material.STAINED_CLAY && block.getData() == 5){
			playerWin(player);
		}
	}
	
}
