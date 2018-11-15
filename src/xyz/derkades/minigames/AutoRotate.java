package xyz.derkades.minigames;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_AQUA;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.games.Game;

public class AutoRotate {

	public static void startNewRandomGame(){
		if (!Minigames.getInstance().getConfig().getBoolean("autorotate")){
			Bukkit.broadcastMessage("***AUTOROTATE DISABLED***");
			return;
		}
		
		if (Bukkit.getOnlinePlayers().size() < 2){
			Bukkit.broadcastMessage(ChatColor.RED + "Minigames will only start with 2 or more players online.");
			Bukkit.broadcastMessage(ChatColor.GOLD + "If you want to have some fun, invite a friend or two!");
			new BukkitRunnable(){
				public void run(){
					startNewRandomGame();
				}
			}.runTaskLater(Minigames.getInstance(), 10*20);
			return;
		}
		
		//If autorotate is enabled and there are at least two players online, continue!
		
		// If a next game is set by a command, do that game next. Otherwise, pick a random game.
		Game game = Minigames.NEXT_GAME == null ? Game.getRandomGame() : Minigames.NEXT_GAME;
		Minigames.NEXT_GAME = null;
		
		String lastGame = Minigames.getInstance().getConfig().getString("last-game");
		//If the randomly selected game is the same as the last game pick a new game
		if (game.getName() == lastGame){
			startNewRandomGame();
			return;
		}
		
		//If there are not enough online players, try again
		if (!(Bukkit.getOnlinePlayers().size() >= game.getRequiredPlayers())){
			// This code will never result in a loop. If there are less than 2 players online, this will not run.
			// There will always be 2 player games.
			startNewRandomGame();
			return;
		}
		
		//Enough players, let's announce and start the game!
		Bukkit.broadcastMessage(DARK_AQUA + "Next minigame: " + AQUA + game.getName());
		
		Minigames.CURRENT_GAME_NAME = game.getName();
		
		new BukkitRunnable(){
			public void run(){
				game.startGame();
			}
		}.runTaskLater(Minigames.getInstance(), 2*20);		
	}

}
