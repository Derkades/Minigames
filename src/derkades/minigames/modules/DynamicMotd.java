package derkades.minigames.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

import derkades.minigames.GameState;
import derkades.minigames.Logger;
import derkades.minigames.games.Game;
import net.kyori.adventure.text.Component;

public class DynamicMotd extends Module {

	@EventHandler
	public void onPing(final ServerListPingEvent event) {
		final String line1 = "Minigames";
		String line2;
		switch(GameState.getCurrentState()) {
			case IDLE -> line2 = "Waiting in lobby";
			case IDLE_MAINTENANCE -> line2 = "Maintenance mode";
			case COUNTDOWN -> {
				final Game<?> game = GameState.getCurrentGame();
				line2 = "Next up: " + game.getName();
			}
			case RUNNING_COUNTDOWN -> {
				final Game<?> game = GameState.getCurrentGame();
				line2 = "Starting soon: " + game.getName();
			}
			case RUNNING_STARTED -> {
				final Game<?> game = GameState.getCurrentGame();
				line2 = "Currently playing " + game.getName();
			}
			case RUNNING_SKIPPED, RUNNING_ENDED_EARLY -> {
				final Game<?> game = GameState.getCurrentGame();
				line2 = "Playing " + game.getName() + " (ending soon)";
			}
			default -> {
				Logger.warning("Unknown game state " + GameState.getCurrentState().name());
				line2 = "Unknown state";
			}
		}
		event.motd(Component.text(line1 + "\n" + line2));
	}

}
