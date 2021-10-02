package derkades.minigames.modules;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

import derkades.minigames.utils.MPlayer;

public class DisableDamage extends Module {

	@EventHandler(priority = EventPriority.LOWEST)
	public void damage(final EntityDamageEvent event) {
		// TODO after removing disableDamage metadata, this condition can be removed and this event can always cancel
		if (event.getEntityType() == EntityType.PLAYER &&
				new MPlayer(event).hasDisabledDamage()) {
			event.setCancelled(true);
		}
	}

}
