package derkades.minigames.modules;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import derkades.minigames.GameState;
import derkades.minigames.menu.MainMenu;

public class LobbyMenu extends Module {

	@EventHandler
	public void gamesMenuOpen(final PlayerInteractEntityEvent event){
		if (GameState.isCurrentlyInGame() || !event.getHand().equals(EquipmentSlot.HAND)) {
			return;
		}

		final Player player = event.getPlayer();

		// 1.16 triggers interact events when clicking items in a menu for some reason
		// We need to ignore these
		// If the player does not have an open inventory, getOpenInventory returns their crafting or creative inventory
		if (player.getOpenInventory().getType() != InventoryType.CRAFTING &&
				player.getOpenInventory().getType() != InventoryType.CREATIVE) {
			return;
		}

		if (event.getRightClicked() instanceof Villager){
			event.setCancelled(true);
			new MainMenu(player);
		}
	}

	@EventHandler
	public void onInteract(final PlayerInteractEvent event) {
		if (!GameState.isCurrentlyInGame() &&
				event.getHand() == EquipmentSlot.HAND &&
				event.getPlayer().getGameMode() == GameMode.ADVENTURE &&
				event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.COMPARATOR)) {
			event.setCancelled(true);
			new MainMenu(event.getPlayer());
		}
	}

	@EventHandler
	public void onEntityDamage(final EntityDamageEvent event) {
		if (event.getEntity() instanceof final Villager villager){
			if (villager.getCustomName().equals("Bait") ||
					villager.getCustomName().contentEquals("Click Me!")) {
				event.setCancelled(true);
			}
		}
	}

}
