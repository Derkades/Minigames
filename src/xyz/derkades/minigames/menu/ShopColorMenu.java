package xyz.derkades.minigames.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;

public class ShopColorMenu extends IconMenu {

	public ShopColorMenu(Player player) {
		super(Minigames.getInstance(), "Colors shop", 9, player);
		
		items.put(0, new ItemBuilder(Material.CYAN_DYE).name(ChatColor.DARK_AQUA + "Cyan").create());
		items.put(1, new ItemBuilder(Material.LAPIS_LAZULI).name(ChatColor.BLUE + "Blue").create());
		items.put(2, new ItemBuilder(Material.ROSE_RED).name(ChatColor.RED + "Red").create());
		items.put(3, new ItemBuilder(Material.ORANGE_DYE).name(ChatColor.GOLD + "Orange").create());
		items.put(4, new ItemBuilder(Material.DANDELION_YELLOW).name(ChatColor.YELLOW + "Yellow").create());
		items.put(5, new ItemBuilder(Material.CACTUS_GREEN).name(ChatColor.DARK_GREEN + "Green").create());
		items.put(6, new ItemBuilder(Material.LIME_DYE).name(ChatColor.GREEN + "Light green").create());
		items.put(7, new ItemBuilder(Material.MAGENTA_DYE).name(ChatColor.LIGHT_PURPLE + "Magenta").create());
		items.put(8, new ItemBuilder(Material.BARRIER).name(ChatColor.RED + "Back").create());
		
	}

	@Override
	public boolean onOptionClick(OptionClickEvent event) {
		Player player = event.getPlayer();
		String name = event.getName();

		if (name.contains("Back")) {
			new ShopMenu(player).open();
			return false;
		}

		ChatColor color = ChatColor.RESET;

		if (name.contains("Cyan")) {
			color = ChatColor.DARK_AQUA;
		} else if (name.contains("Blue")) {
			color = ChatColor.BLUE;
		} else if (name.contains("Red")) {
			color = ChatColor.RED;
		} else if (name.contains("Orange")) {
			color = ChatColor.GOLD;
		} else if (name.contains("Yellow")) {
			color = ChatColor.YELLOW;
		} else if (name.contains("Green")) {
			color = ChatColor.DARK_GREEN;
		} else if (name.contains("Light green")) {
			color = ChatColor.GREEN;
		} else if (name.contains("Magenta")) {
			color = ChatColor.LIGHT_PURPLE;
		}

		if (Points.getPoints(player) >= 250) {
			Points.removePoints(player, 250);
			player.sendMessage(ChatColor.AQUA + "Changed color to " + name.toLowerCase() + ".");
			NameColor.setNameColor(player, color + "");
		} else {
			player.sendMessage(ChatColor.RED + "Not enough points.");
		}
		
		return false;
	}

}
