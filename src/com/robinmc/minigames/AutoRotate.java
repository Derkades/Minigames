package com.robinmc.minigames;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import static org.bukkit.ChatColor.*;

import com.robinmc.minigames.games.Game;
import com.robinmc.minigames.games.ParkourGame;
import com.robinmc.minigames.utils.java.Random;

public class AutoRotate {

	public static void startNewRandomGame(){
		if (!Main.getInstance().getConfig().getBoolean("autorotate")){
			Bukkit.broadcastMessage("***AUTOROTATE DISABLED***");
			return;
		}
		
		if (Bukkit.getOnlinePlayers().size() == 0){
			Bukkit.broadcastMessage("No players online! Trying again in 10 seconds...");
			new BukkitRunnable(){
				public void run(){
					startNewRandomGame();
				}
			}.runTaskLater(Main.getInstance(), 10*20);
			return; //Not sure if this return statement is required, but I'll leave it here.
		}
		
		//If autorotate is enabled and there is at least one player online, continue!
		
		Game game;
		
		if (Main.NEXT_GAME == null){
			game = Game.getRandomGame();
		} else {
			game = Main.NEXT_GAME;
		}
		
		String lastGame = Main.getInstance().getConfig().getString("last-game");
		//If the randomly selected game is the same as the last game pick a new game
		if (game.getName() == lastGame){
			startNewRandomGame();
			return;
		}
		
		//If the game is a parkour game, there's a 50% of selecting a new game
		if (game instanceof ParkourGame){
			boolean bool = Random.getRandomBoolean();
			if (bool){
				startNewRandomGame();
				return;
			}
			//Else: continue..
		}
		
		//If there are not enough online players
		if (!(Bukkit.getOnlinePlayers().size() >= game.getRequiredPlayers())){
			Bukkit.broadcastMessage(RED + "Not enough players to start " + game.getName() + ". Trying another game in 2 seconds...");
			new BukkitRunnable(){
				public void run(){
					startNewRandomGame();
				}
			}.runTaskLater(Main.getInstance(), 2*20);
			return;
		}
		
		//Enough players, let's announce and start the game!
		Bukkit.broadcastMessage(DARK_AQUA + "Next minigame: " + AQUA + game.getName());
		
		new BukkitRunnable(){
			public void run(){
				game.startGame();
			}
		}.runTaskLater(Main.getInstance(), 2*20);
		
		Main.NEXT_GAME = null;
	}

}
