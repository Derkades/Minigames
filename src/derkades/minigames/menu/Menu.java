package derkades.minigames.menu;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

public class Menu {

	static final ItemStack BACK_BUTTON = new ItemBuilder(Material.BARRIER)
			.name(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Back")
			.lore(ChatColor.RED + "Click to go back")
			.create();

	static final ItemStack CLOSE_BUTTON = new ItemBuilder(Material.BARRIER)
			.name(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Close")
			.lore(ChatColor.RED + "Click to close the menu")
			.create();

	static final ItemStack PREV_BUTTON = new ItemBuilder(Material.ARROW)
			.name(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Previous")
			.lore(ChatColor.GRAY + "Go to the previous page")
			.create();

	static final ItemStack NEXT_BUTTON = new ItemBuilder(Material.ARROW)
			.name(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Next")
			.lore(ChatColor.GRAY + "Go to the next page")
			.create();

}
