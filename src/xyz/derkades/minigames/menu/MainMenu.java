package xyz.derkades.minigames.menu;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.YELLOW;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;

public class MainMenu extends IconMenu {

	public MainMenu(final Player player) {
		super(Minigames.getInstance(), "Menu", 1, player);
		addItems();
	}

	public void addItems() {
		addItem(0, new ItemBuilder(Material.GRAY_DYE).name(GOLD + "Points").lore(GRAY + "You have " + YELLOW + Points.getPoints(this.player) + GRAY + " points.", GRAY + "Click for more information.").create());
		addItem(1, new ItemBuilder(Material.GRAY_DYE).name(GOLD + "Games").lore(GRAY + "Click to open games list menu.").create());

		Material settingMaterial;
		if (Minigames.getInstance().getConfig().getStringList("disabled-description").contains(this.player.getUniqueId().toString())) {
			settingMaterial = Material.RED_DYE;
		} else {
			settingMaterial = Material.LIME_DYE;
		}

		addItem(2, new ItemBuilder(settingMaterial).name(GOLD + "Game descriptions").lore(GRAY + "Click to enable or disable game", GRAY + "description messages at the start of each game").create());

		addItem(8, Menu.CLOSE_BUTTON);
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		if (event.getName().contains("Games")){
			new GamesListMenu(event.getPlayer());
			return false;
		} else if (event.getName().contains("Points")) {
			new PointsListMenu(event.getPlayer());
			return false;
		} else if (event.getName().contains("Close")){
			return true;
		} else if (event.getName().contains("Game descriptions")) {
			final Player player = event.getPlayer();

			final ItemStack item = event.getItemStack();
			final List<String> list = Minigames.getInstance().getConfig().getStringList("disabled-description");

			if (item.getType().equals(Material.LIME_DYE)) {
				player.sendMessage(ChatColor.GOLD + "Minigame descriptions have been disabled.");
				list.add(player.getUniqueId().toString());
			} else {
				player.sendMessage(ChatColor.GOLD + "Minigame descriptions have been enabled.");
				list.remove(player.getUniqueId().toString());
			}

			Minigames.getInstance().getConfig().set("disabled-description", list);
			Minigames.getInstance().saveConfig();

			this.addItems();

			return false;
		} else {
			event.getPlayer().sendMessage("error");
			return false;
		}
	}

}
