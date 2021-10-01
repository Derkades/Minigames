package derkades.minigames.modules;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.derkades.derkutils.bukkit.MaterialLists;

import java.util.HashSet;
import java.util.Set;

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
		Block clicked = event.getClickedBlock();
		if (event.getPlayer().getGameMode() == GameMode.ADVENTURE &&
				event.getAction() == Action.RIGHT_CLICK_BLOCK &&
				clicked != null &&
				CANCEL_INTERACT.contains(clicked.getType())) {
			event.setCancelled(true);
		}
	}

}
