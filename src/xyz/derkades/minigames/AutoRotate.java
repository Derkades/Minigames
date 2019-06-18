package xyz.derkades.minigames;

import static org.bukkit.ChatColor.RED;

import org.bukkit.Bukkit;

import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.utils.Scheduler;

public class AutoRotate {

	public static void startNewRandomGame(){
		if (!Minigames.getInstance().getConfig().getBoolean("autorotate")){
			Bukkit.broadcastMessage("[System] AutoRotate disabled, not starting a new command");
			return;
		}

		if (Minigames.STOP_GAMES){
			Scheduler.delay(1*20, () -> {
				Bukkit.broadcastMessage(RED + "An admin stopped the next game from starting. This is probably because some maintenance needs to be done.");
			});
			Minigames.STOP_GAMES = false;
			return;
		}

		if (Bukkit.getOnlinePlayers().size() < 1) {
			Bukkit.broadcastMessage("[System] No players online, waiting 5 seconds");
			Scheduler.delay(5*20, AutoRotate::startNewRandomGame);
			return;
		}

		// If a next game is set by a command, do that game next. Otherwise, pick a random game.
		final Game game = Minigames.NEXT_GAME == null ? Game.getRandomGame() : Minigames.NEXT_GAME;


		// If the randomly selected game is the same as the last game pick a new game
		// Skip check when next minigame has been set
		if (Minigames.NEXT_GAME == null && game.getName() == Minigames.LAST_GAME_NAME){
			startNewRandomGame();
			return;
		}

		Minigames.NEXT_GAME = null;

		//If there are not enough online players, try again
		if (Bukkit.getOnlinePlayers().size() < game.getRequiredPlayers() && !Minigames.BYPASS_PLAYER_MINIMUM_CHECKS){
			startNewRandomGame();
			return;
		}

		//Enough players, let's start the game

		Minigames.CURRENT_GAME_NAME = game.getName();

		Minigames.BYPASS_PLAYER_MINIMUM_CHECKS = false;

		game.startGame();
	}

}
