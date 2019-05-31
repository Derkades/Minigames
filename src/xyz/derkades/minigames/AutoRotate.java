package xyz.derkades.minigames;

import static org.bukkit.ChatColor.RED;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.utils.Scheduler;

public class AutoRotate {

	private static final int MINIMUM_PLAYERS = 2;

	public static void startNewRandomGame(){
		if (!Minigames.getInstance().getConfig().getBoolean("autorotate")){
			Bukkit.broadcastMessage("***AUTOROTATE DISABLED***");
			return;
		}

		if (Minigames.STOP_GAMES){
			Scheduler.delay(1*20, () -> {
				Bukkit.broadcastMessage(RED + "An admin stopped the next game from starting. This is probably because some maintenance needs to be done.");
			});
			Minigames.STOP_GAMES = false;
			return;
		}

		if (Bukkit.getOnlinePlayers().size() < MINIMUM_PLAYERS && !Minigames.BYPASS_PLAYER_MINIMUM_CHECKS){
			Bukkit.broadcastMessage(ChatColor.RED + "Minigames will only start with 2 or more players online.");
			Bukkit.broadcastMessage(ChatColor.GOLD + "If you want to have some fun, invite a friend or two!");
			Scheduler.delay(10*20, AutoRotate::startNewRandomGame);
			return;
		}

		// If a next game is set by a command, do that game next. Otherwise, pick a random game.
		final Game game = Minigames.NEXT_GAME == null ? Game.getRandomGame() : Minigames.NEXT_GAME;
		Minigames.NEXT_GAME = null;

		//If the randomly selected game is the same as the last game pick a new game
		if (game.getName() == Minigames.LAST_GAME_NAME){
			startNewRandomGame();
			return;
		}

		//If there are not enough online players, try again
		if (!(Bukkit.getOnlinePlayers().size() >= game.getRequiredPlayers()) && !Minigames.BYPASS_PLAYER_MINIMUM_CHECKS){
			// This code will never result in a loop. If there are less than 2 players online, this will not run.
			// There will always be 2 player games.
			startNewRandomGame();
			return;
		}

		//Enough players, let's start the game

		Minigames.CURRENT_GAME_NAME = game.getName();

		Minigames.BYPASS_PLAYER_MINIMUM_CHECKS = false;

		game.startGame();
	}

}
