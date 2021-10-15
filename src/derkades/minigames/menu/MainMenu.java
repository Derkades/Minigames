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

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class MainMenu extends IconMenu {

	public MainMenu(final Player player) {
		super(Minigames.getInstance(), text("Menu"), 1, player);
		addItems(player);
	}

	public void addItems(final Player player) {
		addItem(0,
				new ItemBuilder(Material.GRAY_DYE)
						.name(text("Points", GOLD))
						.lore(
								text("You have ", GRAY).append(text(Points.getPoints(player), YELLOW)).append(text(" points.", GRAY)),
								text("Click for more information.", GRAY)
						)
						.create()
		);
		addItem(1,
				new ItemBuilder(Material.GRAY_DYE)
						.name(text("Games", GOLD))
						.lore(text("Click to open games list menu.", GRAY))
						.create()
		);

		Material settingMaterial;
		if (Minigames.getInstance().getConfig().getStringList("disabled-description").contains(player.getUniqueId().toString())) {
			settingMaterial = Material.RED_DYE;
		} else {
			settingMaterial = Material.LIME_DYE;
		}

		addItem(2,
				new ItemBuilder(settingMaterial)
						.name(text("Game descriptions", GOLD))
						.lore(
								text("Click to enable or disable game", GRAY),
								text("description messages at the start of each game", GRAY))
						.create());

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
			Minigames.getInstance().queueConfigSave();

			this.addItems(event.getPlayer());

			return false;
		} else {
			Logger.warning("Unhandled menu click slot %s", event.getPosition());
			return false;
		}
	}

}
