package derkades.minigames.modules;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import xyz.derkades.derkutils.bukkit.MaterialLists;

public class CancelInteract extends Module {

	private static final Set<Material> CANCEL_INTERACT = new HashSet<>();

	static {
		CANCEL_INTERACT.addAll(MaterialLists.TRAPDOORS);
		CANCEL_INTERACT.addAll(MaterialLists.DOORS);
		CANCEL_INTERACT.addAll(MaterialLists.FENCE_GATES);
		CANCEL_INTERACT.addAll(MaterialLists.FLOWER_POTS);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInteract(final PlayerInteractEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.ADVENTURE &&
				event.getAction() == Action.RIGHT_CLICK_BLOCK &&
				CANCEL_INTERACT.contains(event.getClickedBlock().getType())) {
			event.setCancelled(true);
		}
	}

}
