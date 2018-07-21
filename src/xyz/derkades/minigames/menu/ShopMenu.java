package xyz.derkades.minigames.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;
import xyz.derkades.minigames.Minigames;

public class ShopMenu extends IconMenu {

	public ShopMenu(Player player) {
		super(Minigames.getInstance(), "Shop", 9, player);
		
		items.put(0, new ItemBuilder(Material.INK_SACK).data(12).name(ChatColor.AQUA + "Name colors (250 points for every color)").create());
		items.put(8, new ItemBuilder(Material.BARRIER).coloredName("&cBack").create());
	}

	@Override
	public boolean onOptionClick(OptionClickEvent event) {
		Player player = event.getPlayer();
		String name = event.getName();

		if (name.contains("color")){
			new ShopColorMenu(player).open();
		} else {
			new MainMenu(player).open();
		}
		
		return false;
	}

}
