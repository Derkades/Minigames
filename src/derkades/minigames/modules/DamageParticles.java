package derkades.minigames.modules;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageParticles extends Module {

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		Location head = event.getEntity().getLocation().add(0, 1.8, 0);
//		int damageCount = (int) (event.getDamage() / 2);
//		head.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, head, 3, .2f, 0, .2f);
		BlockData blockData = Material.RED_CONCRETE_POWDER.createBlockData();
		head.getWorld().spawnParticle(Particle.BLOCK_DUST, event.getEntity().getLocation().add(0, 1, 0), 10, 0.1, .5, 0.1, blockData);
	}

}
