package derkades.minigames.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import derkades.minigames.utils.MPlayer;

public class RandomNameColor extends Module {

	@EventHandler(priority = EventPriority.LOWEST) // needs to run before any other events that use display name
	public void onJoin(final PlayerJoinEvent event) {
		new MPlayer(event).setDisplayName(null);
	}

}
