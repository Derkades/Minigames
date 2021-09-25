package derkades.minigames.modules;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.utils.PluginLoadEvent;

public class ResetPlayersOnEnable extends Module {

	@EventHandler
	public void onLoad(final PluginLoadEvent event) {
		if (Logger.debugModeEnabled()) {
			Logger.info("Debug mode is enabled, only going to reset players who are not in creative mode.");
			Minigames.getOnlinePlayers().stream().filter(p -> p.getGameMode() != GameMode.CREATIVE).forEach((p) -> {
				Logger.debug("Resetting player %s (debug mode, not creative)", p.getName());
//				p.applyLobbySettings();
//				p.queueTeleport(Var.LOBBY_LOCATION);
//				p.teleportLobbyAsync();
				p.queueLobbyTeleport();
			});
		} else {
			Minigames.getOnlinePlayers().forEach((p) -> {
				Logger.debug("Resetting player %s (no debug mode)", p.getName());
//				p.applyLobbySettings();
//				p.queueTeleport(Var.LOBBY_LOCATION);
				p.queueLobbyTeleport();
			});
		}
	}

}
