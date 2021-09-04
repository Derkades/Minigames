package derkades.minigames.modules;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import derkades.minigames.utils.MPlayer;

public class DisableInventoryItemMove extends Module {

	@EventHandler
	public void inventoryClickEvent(final InventoryClickEvent event) {
		final MPlayer player = new MPlayer(event.getView().getPlayer());
		if (player.getDisableItemMoving() && player.getGameMode().equals(GameMode.ADVENTURE)) {
			event.setCancelled(true);
		}
	}

}
