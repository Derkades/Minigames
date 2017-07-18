package xyz.derkades.minigames;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.games.ParkourGame;
import xyz.derkades.minigames.utils.java.Random;

import static org.bukkit.ChatColor.*;

public class AutoRotate {

	public static void startNewRandomGame(){
		if (!Minigames.getInstance().getConfig().getBoolean("autorotate")){
			Bukkit.broadcastMessage("***AUTOROTATE DISABLED***");
			return;
		}
		
		if (Bukkit.getOnlinePlayers().size() == 0){
			Bukkit.broadcastMessage("No players online! Trying again in 10 seconds...");
			new BukkitRunnable(){
				public void run(){
					startNewRandomGame();
				}
			}.runTaskLater(Minigames.getInstance(), 10*20);
			return; //Not sure if this return statement is required, but I'll leave it here.
		}
		
		//If autorotate is enabled and there is at least one player online, continue!
		
		Game game;
		
		if (Minigames.NEXT_GAME == null){
			game = Game.getRandomGame();
		} else {
			game = Minigames.NEXT_GAME;
		}
		
		String lastGame = Minigames.getInstance().getConfig().getString("last-game");
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
			}.runTaskLater(Minigames.getInstance(), 2*20);
			return;
		}
		
		//Enough players, let's announce and start the game!
		Bukkit.broadcastMessage(DARK_AQUA + "Next minigame: " + AQUA + game.getName());
		
		new BukkitRunnable(){
			public void run(){
				game.startGame();
			}
		}.runTaskLater(Minigames.getInstance(), 2*20);
		
		Minigames.NEXT_GAME = null;
	}

}
