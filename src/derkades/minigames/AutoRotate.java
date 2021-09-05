package derkades.minigames;

import org.bukkit.Bukkit;

import derkades.minigames.games.Game;
import derkades.minigames.games.GameMap;
import derkades.minigames.random.RandomPicking;
import derkades.minigames.utils.Scheduler;
import net.kyori.adventure.text.Component;
import xyz.derkades.derkutils.bukkit.StandardTextColor;

public class AutoRotate {

	public static void startNewRandomGame() {
		if (!Minigames.getInstance().getConfig().getBoolean("autorotate")) {
			Bukkit.broadcast(Component.text("AutoRotate disabled, not starting a new game"));
			return;
		}

		if (Minigames.STOP_GAMES) {
			Bukkit.broadcast(Component.text("An admin stopped the next game from starting. This is probably because some maintenance needs to be done.").color(StandardTextColor.RED));
			Minigames.STOP_GAMES = false;
			GameState.setState(GameState.IDLE_MAINTENANCE);
			return;
		}

		if (Bukkit.getOnlinePlayers().size() < 2) {
			Bukkit.broadcast(Component.text("Not enough players to start a game.").color(StandardTextColor.RED));
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
