package derkades.minigames.menu;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.derkades.derkutils.bukkit.ItemBuilder;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public class Menu {

	static final ItemStack BACK_BUTTON = new ItemBuilder(Material.BARRIER)
			.name(text("Back", DARK_RED).decorate(BOLD))
			.lore(text("Click to go back", RED))
			.create();

	static final ItemStack CLOSE_BUTTON = new ItemBuilder(Material.BARRIER)
			.name(text("Close", DARK_RED).decorate(BOLD))
			.lore(text("Click to close the menu", RED))
			.create();

	static final ItemStack PREV_BUTTON = new ItemBuilder(Material.ARROW)
			.name(text("Previous", GOLD).decorate(BOLD))
			.lore(text("Go to the previous page", GRAY))
			.create();

	static final ItemStack NEXT_BUTTON = new ItemBuilder(Material.ARROW)
			.name(text("Next", GOLD).decorate(BOLD))
			.lore(text("Go to the next page", GRAY))
			.create();

}
