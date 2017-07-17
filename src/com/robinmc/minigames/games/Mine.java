package com.robinmc.minigames.games;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.robinmc.minigames.Main;

import net.md_5.bungee.api.ChatColor;

public class Mine extends Game {
	
	private void addPoints(Player player, int points){
		int oldPoints = objective.getScore(ChatColor.AQUA + player.getName()).getScore();
		objective.getScore(ChatColor.AQUA + player.getName()).setScore(oldPoints + points);
	}
	
	private Map<Material, Integer> blockPoints = new HashMap<Material, Integer>();
	
	private void initializeBlockPointsHashMap(){
		blockPoints.put(Material.COAL_ORE, 1);
		blockPoints.put(Material.IRON_ORE, 2);
		blockPoints.put(Material.GOLD_ORE, 5);
		blockPoints.put(Material.DIAMOND_ORE, 10);
		blockPoints.put(Material.EMERALD_ORE, 15);
		blockPoints.put(Material.BEACON, 20);
		blockPoints.put(Material.NETHERRACK, -20);
	}
	
	private ScoreboardManager manager;
	private Scoreboard board;
	private Objective objective;
	
	@Override
	String[] getDescription() {
		return new String[]{
				"Insert description here"
				};
	}

	@Override
	public String getName() {
		return "Mine";
	}

	@Override
	public int getRequiredPlayers() {
		return 1; //TODO Change required players to 2
	}

	@Override
	public GamePoints getPoints() {
		return new GamePoints(3, 6);
	}

	@Override
	public void resetHashMaps(Player player) {
		//No hashmaps!
	}

	@Override
	void begin() {
		initializeBlockPointsHashMap();
		initializeScoreboard();
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
		}.runTaskLater(Main.getInstance(), 50*20);
	}
	
	private void endGame(){
		resetScoreboard();
	}
	
	private void initializeScoreboard(){
		manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
		objective = board.registerNewObjective("points", "dummy");
		objective.setDisplayName(ChatColor.DARK_AQUA + "Points");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for (Player player : Bukkit.getOnlinePlayers()){
			objective.getScore(player.getName()).setScore(0);
		}
	}
	
	private void resetScoreboard(){
		objective.unregister();
		board.clearSlot(DisplaySlot.SIDEBAR);
		
		for (Player player : Bukkit.getOnlinePlayers()) board.resetScores(ChatColor.AQUA + player.getName());
		
		manager = null;
		board = null;
		objective = null;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if (!isRunning()) return;
		
		Material type = event.getBlock().getType();
		if (blockPoints.containsKey(type)){
			int points = blockPoints.get(type);
			addPoints(event.getPlayer(), points);
		}
	}

}
