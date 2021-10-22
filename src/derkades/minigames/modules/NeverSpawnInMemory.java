package derkades.minigames.modules;

import derkades.minigames.Var;
import derkades.minigames.utils.PluginLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldInitEvent;

public class NeverSpawnInMemory extends Module {

	@EventHandler
	public void onLoad(PluginLoadEvent event) {
		Var.LOBBY_WORLD.setKeepSpawnInMemory(false);
	}

	@EventHandler
	public void onWorldInit(final WorldInitEvent event) {
		event.getWorld().setKeepSpawnInMemory(false);
	}

}
