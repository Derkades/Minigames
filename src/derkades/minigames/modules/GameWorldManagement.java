package derkades.minigames.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldInitEvent;

import derkades.minigames.Var;

public class GameWorldManagement extends Module {

	@EventHandler
	public void onWorldInit(final WorldInitEvent event) {
		if (!event.getWorld().equals(Var.LOBBY_WORLD)) {
			event.getWorld().setKeepSpawnInMemory(false);
		}
	}

}
