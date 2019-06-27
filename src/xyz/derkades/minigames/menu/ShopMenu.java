package xyz.derkades.minigames.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;
import xyz.derkades.minigames.Minigames;

public class ShopMenu extends IconMenu {

	public ShopMenu(final Player player) {
		super(Minigames.getInstance(), "Shop", 9, player);

		final String price = Minigames.economy.format(250);

		this.items.put(0, new ItemBuilder(Material.GRAY_DYE).name(ChatColor.AQUA + "Name colors (" + price + " for every color)").create());
		this.items.put(8, Menu.BACK_BUTTON);
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		final Player player = event.getPlayer();
		final String name = event.getName();

		if (name.contains("color")){
			new ShopColorMenu(player).open();
		} else {
			new MainMenu(player).open();
		}

		return false;
	}

}
