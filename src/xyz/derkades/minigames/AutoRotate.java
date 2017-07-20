package xyz.derkades.minigames;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.RED;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.games.ParkourGame;

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
		
		//If the game is a parkour game, there's a 75% of selecting a new game
		if (game instanceof ParkourGame){
			boolean bool = Random.getRandomInteger(0, 3) < 1;
			if (bool){
				startNewRandomGame();
				return;
			}
			//Else: continue..
		}
		
		//If there are not enough online players, try again
		if (!(Bukkit.getOnlinePlayers().size() >= game.getRequiredPlayers())){
			startNewRandomGame();
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
