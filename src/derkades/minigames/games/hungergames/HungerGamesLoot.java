package derkades.minigames.games.hungergames;

import org.bukkit.Color;
import org.bukkit.Material;

import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.lootchests.LootItem;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

class HungerGamesLoot {

	static final LootItem[] LOOT_1 = {
			new LootItem(Material.WOODEN_SWORD, 0.6f),
			new LootItem(Material.STONE_SWORD, 0.5f),
			new LootItem(Material.IRON_SWORD, 0.1f),

			new LootItem(Material.LEATHER_BOOTS, 0.3f),
			new LootItem(Material.LEATHER_LEGGINGS, 0.3f),
			new LootItem(Material.LEATHER_CHESTPLATE, 0.3f),
			new LootItem(Material.LEATHER_HELMET, 0.3f),

			new LootItem(Material.CHAINMAIL_BOOTS, 0.1f),
			new LootItem(Material.CHAINMAIL_LEGGINGS, 0.1f),
			new LootItem(Material.CHAINMAIL_CHESTPLATE, 0.1f),
			new LootItem(Material.CHAINMAIL_HELMET, 0.1f),

			new LootItem(Material.GOLDEN_BOOTS, 0.15f),
			new LootItem(Material.GOLDEN_LEGGINGS, 0.15f),
			new LootItem(Material.GOLDEN_CHESTPLATE, 0.15f),
			new LootItem(Material.GOLDEN_HELMET, 0.15f),

			new LootItem(Material.COOKED_CHICKEN, 1, 5, 0.3f),

			new LootItem(Material.GOLDEN_SWORD, 0.3f),
			new LootItem(Material.MELON_SLICE, 3, 15, 0.6f),
			new LootItem(Material.ARROW, 1, 3, 0.8f),
			new LootItem(Material.ARROW, 6, 12, 0.2f),
	};

	static final LootItem[] LOOT_2 = {
			new LootItem(Material.COOKED_BEEF, 1, 3, 0.7f),
			new LootItem(Material.IRON_SWORD, 0.4f),
			new LootItem(Material.DIAMOND_BOOTS, 0.4f),
			new LootItem(Material.BOW, 0.8f),

			new LootItem(Material.CHAINMAIL_BOOTS, 0.2f),
			new LootItem(Material.CHAINMAIL_LEGGINGS, 0.2f),
			new LootItem(Material.CHAINMAIL_CHESTPLATE, 0.2f),
			new LootItem(Material.CHAINMAIL_HELMET, 0.2f),

			new LootItem(Material.GOLDEN_BOOTS, 0.15f),
			new LootItem(Material.GOLDEN_LEGGINGS, 0.15f),
			new LootItem(Material.GOLDEN_CHESTPLATE, 0.15f),
			new LootItem(Material.GOLDEN_HELMET, 0.15f),

			new LootItem(Material.IRON_BOOTS, 0.1f),
			new LootItem(Material.IRON_LEGGINGS, 0.1f),
			new LootItem(Material.IRON_CHESTPLATE, 0.1f),
			new LootItem(Material.IRON_HELMET, 0.1f),
			new LootItem(new ItemBuilder(Material.LEATHER_HELMET).leatherArmorColor(Color.FUCHSIA).name(text("Stylish Helmet", GOLD)).lore(text("Makes you look cool but die just as quickly.", GRAY)).create(), 0.05f),
	};

}
