package xyz.derkades.minigames.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;
import xyz.derkades.minigames.Minigames;

public class ShopColorMenu extends IconMenu {

	public ShopColorMenu(final Player player) {
		super(Minigames.getInstance(), "Colors shop", 9, player);

		this.items.put(0, new ItemBuilder(Material.CYAN_DYE).name(ChatColor.DARK_AQUA + "Cyan").create());
		this.items.put(1, new ItemBuilder(Material.LAPIS_LAZULI).name(ChatColor.BLUE + "Blue").create());
		this.items.put(2, new ItemBuilder(Material.RED_DYE).name(ChatColor.RED + "Red").create());
		this.items.put(3, new ItemBuilder(Material.ORANGE_DYE).name(ChatColor.GOLD + "Orange").create());
		this.items.put(4, new ItemBuilder(Material.YELLOW_DYE).name(ChatColor.YELLOW + "Yellow").create());
		this.items.put(5, new ItemBuilder(Material.GREEN_DYE).name(ChatColor.DARK_GREEN + "Green").create());
		this.items.put(6, new ItemBuilder(Material.LIME_DYE).name(ChatColor.GREEN + "Light green").create());
		this.items.put(7, new ItemBuilder(Material.MAGENTA_DYE).name(ChatColor.LIGHT_PURPLE + "Magenta").create());
		this.items.put(8, new ItemBuilder(Material.BARRIER).name(ChatColor.RED + "Back").create());

	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		final Player player = event.getPlayer();
		final String name = event.getName();

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

		if (Minigames.economy.getBalance(player) >= 250) {
			Minigames.economy.withdrawPlayer(player, 250);
			player.sendMessage(ChatColor.AQUA + "Changed color to " + name.toLowerCase() + ".");
			NameColor.setNameColor(player, color + "");
			return true;
		} else {
			player.sendMessage(ChatColor.RED + "Not enough money.");
			return true;
		}
	}

}
