package derkades.minigames.modules;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class CancelInteract extends Module {

	private static final List<Tag<Material>> CANCEL_INTERACT_TAGS = List.of(
			Tag.TRAPDOORS,
			Tag.DOORS,
			Tag.FENCE_GATES,
			Tag.FLOWER_POTS
	);

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInteract(final PlayerInteractEvent event) {
		Block clicked = event.getClickedBlock();
		if (event.getPlayer().getGameMode() == GameMode.ADVENTURE &&
				event.getAction() == Action.RIGHT_CLICK_BLOCK &&
				clicked != null) {
			for (final Tag<Material> tag : CANCEL_INTERACT_TAGS) {
				if (tag.isTagged(clicked.getType())) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}

}
