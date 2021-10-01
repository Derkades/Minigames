package derkades.minigames.menu;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.Points;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

import java.util.List;

import static org.bukkit.ChatColor.*;

public class MainMenu extends IconMenu {

	public MainMenu(final Player player) {
		super(Minigames.getInstance(), "Menu", 1, player);
		addItems(player);
	}

	public void addItems(final Player player) {
		addItem(0, new ItemBuilder(Material.GRAY_DYE).name(GOLD + "Points").lore(GRAY + "You have " + YELLOW + Points.getPoints(player) + GRAY + " points.", GRAY + "Click for more information.").create());
		addItem(1, new ItemBuilder(Material.GRAY_DYE).name(GOLD + "Games").lore(GRAY + "Click to open games list menu.").create());

		Material settingMaterial;
		if (Minigames.getInstance().getConfig().getStringList("disabled-description").contains(player.getUniqueId().toString())) {
			settingMaterial = Material.RED_DYE;
		} else {
			settingMaterial = Material.LIME_DYE;
		}

		addItem(2, new ItemBuilder(settingMaterial).name(GOLD + "Game descriptions").lore(GRAY + "Click to enable or disable game", GRAY + "description messages at the start of each game").create());

		addItem(8, Menu.CLOSE_BUTTON);
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		String name = event.getName();
		if (name == null) {
			return false;
		}

		if (name.contains("Games")){
			new GamesListMenu(event.getPlayer());
			return false;
		} else if (name.contains("Points")) {
			new PointsListMenu(event.getPlayer());
			return false;
		} else if (name.contains("Close")){
			return true;
		} else if (name.contains("Game descriptions")) {
			final Player player = event.getPlayer();

			final ItemStack item = event.getItemStack();
			final List<String> list = Minigames.getInstance().getConfig().getStringList("disabled-description");

			if (item != null &&
					item.getType() == Material.LIME_DYE) {
				player.sendMessage(ChatColor.GOLD + "Minigame descriptions have been disabled.");
				list.add(player.getUniqueId().toString());
			} else {
				player.sendMessage(ChatColor.GOLD + "Minigame descriptions have been enabled.");
				list.remove(player.getUniqueId().toString());
			}

			Minigames.getInstance().getConfig().set("disabled-description", list);
			Minigames.getInstance().saveConfig();

			this.addItems(event.getPlayer());

			return false;
		} else {
			Logger.warning("Unhandled menu click slot %s", event.getPosition());
			return false;
		}
	}

}
