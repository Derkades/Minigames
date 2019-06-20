package xyz.derkades.minigames.games.hungergames;

import org.bukkit.Color;
import org.bukkit.Material;

import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.lootchests.LootItem;

public class HungerGamesLoot {

	public static final LootItem[] LOOT_1 = {
			new LootItem(Material.COOKED_CHICKEN, 1, 5, 0.3f),
			new LootItem(Material.STONE_SWORD, 0.5f),
			new LootItem(Material.GOLDEN_SWORD, 0.3f),
			new LootItem(Material.LEATHER_HELMET, 0.8f),
			new LootItem(Material.CHAINMAIL_CHESTPLATE, 0.6f),
			new LootItem(Material.MELON_SLICE, 3, 15, 0.6f),
			new LootItem(Material.ARROW, 1, 6, 0.5f),
	};

	public static final LootItem[] LOOT_2 = {
			new LootItem(Material.COOKED_BEEF, 1, 3, 0.7f),
			new LootItem(Material.IRON_SWORD, 0.6f),
			new LootItem(Material.DIAMOND_BOOTS, 0.4f),
			new LootItem(Material.BOW, 0.8f),
			new LootItem(new ItemBuilder(Material.LEATHER_HELMET).leatherArmorColor(Color.FUCHSIA).name("Stylish Helmet").lore("Makes you look cool but die just as quickly.").create(), 0.5f),
	};

}
