package derkades.minigames;

import static org.bukkit.ChatColor.RED;

import org.bukkit.Bukkit;

import derkades.minigames.games.Game;
import derkades.minigames.games.GameMap;
import derkades.minigames.random.RandomPicking;
import derkades.minigames.utils.Scheduler;
import net.md_5.bungee.api.ChatColor;

public class AutoRotate {

	public static void startNewRandomGame() {
		if (!Minigames.getInstance().getConfig().getBoolean("autorotate")) {
			Bukkit.broadcastMessage("AutoRotate disabled, not starting a new game");
			return;
		}

		if (Minigames.STOP_GAMES) {
			Bukkit.broadcastMessage(RED + "An admin stopped the next game from starting. This is probably because some maintenance needs to be done.");
			Minigames.STOP_GAMES = false;
			GameState.setState(GameState.IDLE_MAINTENANCE);
			return;
		}

		if (Bukkit.getOnlinePlayers().size() < 2) {
			Bukkit.broadcastMessage(ChatColor.RED + "Not enough players to start a game.");
			Scheduler.delay(8*20, AutoRotate::startNewRandomGame);
			return;
		}

		final Game<? extends GameMap> game = RandomPicking.getRandomGame();

		// If there are not enough online players, try again
		if (Bukkit.getOnlinePlayers().size() < game.getRequiredPlayers() && !Minigames.BYPASS_PLAYER_MINIMUM_CHECKS) {
			startNewRandomGame();
			return;
		}

		// Enough players, let's start the game

		Minigames.BYPASS_PLAYER_MINIMUM_CHECKS = false;

		game.start();
	}

}
