package xyz.derkades.minigames;

import static org.bukkit.ChatColor.RED;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.random.RandomPicking;
import xyz.derkades.minigames.utils.Scheduler;

public class AutoRotate {

	public static void startNewRandomGame(){
		if (!Minigames.getInstance().getConfig().getBoolean("autorotate")){
			Bukkit.broadcastMessage("[System] AutoRotate disabled, not starting a new game");
			return;
		}

		if (Minigames.STOP_GAMES){
			Scheduler.delay(1*20, () -> {
				Bukkit.broadcastMessage(RED + "An admin stopped the next game from starting. This is probably because some maintenance needs to be done.");
			});
			Minigames.STOP_GAMES = false;
			return;
		}

		if (Bukkit.getOnlinePlayers().size() < 2) {
			Bukkit.broadcastMessage(ChatColor.RED + "Not enough players online to start a game. Please invite more players.");
			Scheduler.delay(5*20, AutoRotate::startNewRandomGame);
			return;
		}

		final Game<? extends GameMap> game = RandomPicking.getRandomGame();

		//If there are not enough online players, try again
		if (Bukkit.getOnlinePlayers().size() < game.getRequiredPlayers() && !Minigames.BYPASS_PLAYER_MINIMUM_CHECKS){
			startNewRandomGame();
			return;
		}

		//Enough players, let's start the game

		Minigames.BYPASS_PLAYER_MINIMUM_CHECKS = false;

		game.start();
	}

}
