package derkades.minigames.modules;

import derkades.minigames.utils.MPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class RandomNameColor extends Module {

	@EventHandler(priority = EventPriority.LOWEST) // needs to run before any other events that use display name
	public void onJoin(final PlayerJoinEvent event) {
		new MPlayer(event).setDisplayName(null);
	}

}
