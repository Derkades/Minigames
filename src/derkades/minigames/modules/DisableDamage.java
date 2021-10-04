package derkades.minigames.modules;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

import derkades.minigames.utils.MPlayer;

public class DisableDamage extends Module {

	@EventHandler(priority = EventPriority.LOWEST)
	public void damage(final EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			event.setCancelled(true);
		}
	}

}
