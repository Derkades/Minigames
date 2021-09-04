package derkades.minigames.games.gladeroyale;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.lootchests.LootItem;

class GladeRoyaleItems {

	static final ItemStack BLOCK = new ItemBuilder(Material.TERRACOTTA)
			.canPlaceOn(
					"minecraft:grass_block",
					"minecraft:red_concrete_powder",
					"minecraft:black_concrete_powder",
					"minecraft:sand",
					"minecraft:sandstone",
					"minecraft:light_gray_terracotta",
					"minecraft:terracotta")
			.name(ChatColor.YELLOW + "Block")
			.create();

	static final ItemStack TOOL = new ItemBuilder(Material.IRON_PICKAXE)
			.canDestroy("minecraft:terracotta")
			.name(ChatColor.YELLOW + "Block breaker")
			.enchant(Enchantment.DIG_SPEED, 3)
			.unbreakable()
			.create();

	static final LootItem[] SUPPLY_DROP = {
			new LootItem(BLOCK, 1, 5, 0.9f),
			new LootItem(BLOCK, 1, 15, 0.7f),
			new LootItem(BLOCK, 10, 30, 0.5f),

			new LootItem(Material.WOODEN_SWORD, 0.8f),
			new LootItem(Material.STONE_SWORD, 0.5f),
			new LootItem(Material.IRON_SWORD, 0.3f),
			new LootItem(Material.DIAMOND_SWORD, 0.1f),

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

			new LootItem(Material.GOLDEN_BOOTS, 0.05f),
			new LootItem(Material.GOLDEN_LEGGINGS, 0.05f),
			new LootItem(Material.GOLDEN_CHESTPLATE, 0.05f),
			new LootItem(Material.GOLDEN_HELMET, 0.05f),
	};

}
