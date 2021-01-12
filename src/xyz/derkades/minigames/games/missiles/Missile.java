package xyz.derkades.minigames.games.missiles;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import xyz.derkades.derkutils.bukkit.ItemBuilder;

public enum Missile {
	
	// <-left/+right> <+up/-down> <+front/-back> <material> <facing>
	// facing = 0-down 1-up 2-left 3-right 4-front 5-back

	TEST("Test", Material.STICK, ChatColor.GREEN, new MissileBlock[] {
			// https://www.youtube.com/watch?v=Z4VuRYqlv5Q
			new MissileBlock(0, 0, 0, Material.PISTON, 4),
			new MissileBlock(0, 1, 0, Material.PISTON, 5),
			new MissileBlock(1, 0, 0, Material.SLIME_BLOCK),
			new MissileBlock(1, 1, 0, Material.SLIME_BLOCK),
			new MissileBlock(1, 1, 1, Material.SLIME_BLOCK),
			new MissileBlock(1, 0, 3, Material.PISTON, 4),
			new MissileBlock(1, 1, 3, Material.STICKY_PISTON, 5),
			new MissileBlock(1, 0, 4, Material.SLIME_BLOCK),
			new MissileBlock(1, 0, 5, Material.OBSIDIAN),
			new MissileBlock(1, 1, 4, Material.REDSTONE_BLOCK),
			new MissileBlock(0, 0, 2, Material.SLIME_BLOCK),
			new MissileBlock(0, 1, 2, Material.REDSTONE_BLOCK),
			new MissileBlock(0, 0, 3, Material.TNT),
			new MissileBlock(0, 1, 3, Material.SLIME_BLOCK),
			new MissileBlock(1, 0, 5, Material.AIR),
			new MissileBlock(0, 1, 4, Material.OBSERVER, 5),
			new MissileBlock(1, 0, 5, Material.AIR),
			new MissileBlock(0, 0, 4, Material.TNT),
			new MissileBlock(1, 0, 5, Material.TNT),
			new MissileBlock(0, 0, 5, Material.TNT),
			new MissileBlock(1, 0, 6, Material.TNT),
			new MissileBlock(0, 0, 6, Material.TNT),
			new MissileBlock(0, 1, 5, Material.TNT),
			new MissileBlock(1, 1, 6, Material.TNT),
			new MissileBlock(0, 1, 6, Material.TNT),
			new MissileBlock(1, 1, 5, Material.SLIME_BLOCK),
			new MissileBlock(1, 0, 2, Material.TNT),
	}),
	
	JUGGERNAUT("Juggernaut", Material.TNT, ChatColor.RED, new MissileBlock[] {
			new MissileBlock(0, -1, 1, Material.PISTON, 4),
			new MissileBlock(0, -1, 3, Material.TNT),
			new MissileBlock(0, -1, 4, Material.TNT),
			new MissileBlock(0, -1, 5, Material.TNT),
			new MissileBlock(0, -1, 6, Material.TNT),
			
			new MissileBlock(0, 0, 0, Material.OBSERVER, 5),
			new MissileBlock(0, 0, 1, Material.SLIME_BLOCK),
			new MissileBlock(-1, 0, 1, Material.PISTON, 4),
			new MissileBlock(0, 1, 1, Material.PISTON, 4),
			new MissileBlock(1, 0, 1, Material.PISTON, 4),
			
			new MissileBlock(-1, 0, 3, Material.TNT),
			new MissileBlock(0, 0, 3, Material.STICKY_PISTON, 5),
			new MissileBlock(1, 0, 3, Material.TNT),
			
			new MissileBlock(-1, 0, 4, Material.TNT),
			new MissileBlock(0, 0, 4, Material.TNT),
			new MissileBlock(1, 0, 4, Material.TNT),
			new MissileBlock(-1, 0, 5, Material.TNT),
			new MissileBlock(0, 0, 5, Material.TNT),
			new MissileBlock(1, 0, 5, Material.TNT),
			new MissileBlock(-1, 0, 6, Material.TNT),
			new MissileBlock(0, 0, 6, Material.TNT),
			new MissileBlock(1, 0, 6, Material.TNT),
			
			new MissileBlock(-1, 0, 9, Material.REDSTONE_BLOCK),
			new MissileBlock(0, 0, 9, Material.SLIME_BLOCK),
			new MissileBlock(1, 0, 9, Material.REDSTONE_BLOCK),
			
			new MissileBlock(-1, 1, 3, Material.PISTON, 4),
			new MissileBlock(0, 1, 3, Material.SLIME_BLOCK),
			new MissileBlock(1, 1, 3, Material.PISTON, 4),
			
			new MissileBlock(-1, 1, 4, Material.TNT),
			new MissileBlock(0, 1, 4, Material.OBSERVER, 4),
			new MissileBlock(1, 1, 4, Material.TNT),
			
			new MissileBlock(-1, 1, 5, Material.TNT),
			new MissileBlock(1, 1, 5, Material.TNT),
			
			new MissileBlock(-1, 1, 6, Material.TNT),
			new MissileBlock(0, 1, 6, Material.OBSERVER, 5),
			new MissileBlock(1, 1, 6, Material.TNT),
			
			new MissileBlock(-1, 1, 7, Material.TNT),
			new MissileBlock(0, 1, 7, Material.PISTON, 4),
			new MissileBlock(1, 1, 7, Material.TNT),
			
			new MissileBlock(-1, 1, 8, Material.TNT),
			new MissileBlock(0, 1, 8, Material.TNT),
			new MissileBlock(1, 1, 8, Material.TNT),
			
			new MissileBlock(0, 1, 9, Material.SLIME_BLOCK),
			
			new MissileBlock(0, 2, 3, Material.PISTON, 4),
			
			new MissileBlock(0, 2, 4, Material.TNT),
			new MissileBlock(0, 2, 5, Material.TNT),
			new MissileBlock(0, 2, 6, Material.TNT),
			new MissileBlock(0, 2, 7, Material.TNT),
			
			new MissileBlock(0, 1, 5, Material.TNT),
	}),
	
	COMPRESSOR("Compressor", Material.PISTON, ChatColor.DARK_PURPLE, new MissileBlock[] {
			new MissileBlock(0, 0, 0, Material.TNT),
			new MissileBlock(1, 0, 0, Material.SLIME_BLOCK),
			new MissileBlock(0, 0, 1, Material.TNT),
			new MissileBlock(1, 0, 1, Material.STICKY_PISTON, 5),
			new MissileBlock(0, 0, 2, Material.TNT),
			new MissileBlock(0, 0, 3, Material.WHITE_GLAZED_TERRACOTTA),
			new MissileBlock(0, 0, 4, Material.WHITE_GLAZED_TERRACOTTA),
			new MissileBlock(0, 0, 5, Material.WHITE_GLAZED_TERRACOTTA),
			new MissileBlock(0, 0, 6, Material.WHITE_GLAZED_TERRACOTTA),
			new MissileBlock(0, 0, 7, Material.SLIME_BLOCK),
			new MissileBlock(1, 0, 7, Material.SLIME_BLOCK),
			new MissileBlock(0, 0, 8, Material.REDSTONE_BLOCK),
			new MissileBlock(1, 0, 8, Material.REDSTONE_BLOCK),
			
			new MissileBlock(0, 1, 1, Material.PISTON, 4),
			new MissileBlock(1, 1, 1, Material.SLIME_BLOCK),
			new MissileBlock(1, 2, 1, Material.OBSERVER, 1),
			new MissileBlock(1, 2, 2, Material.TNT),
			new MissileBlock(0, 1, 3, Material.SLIME_BLOCK),
			new MissileBlock(1, 1, 3, Material.STICKY_PISTON, 5),
			new MissileBlock(0, 1, 4, Material.TNT),
			new MissileBlock(1, 1, 4, Material.TNT),
			new MissileBlock(0, 1, 5, Material.TNT),
			new MissileBlock(1, 1, 5, Material.TNT),
			new MissileBlock(0, 1, 6, Material.TNT),
			new MissileBlock(1, 1, 6, Material.TNT),

			new MissileBlock(0, 2, 3, Material.OBSERVER, 2),
			new MissileBlock(-1, 2, 3, Material.GLASS),
			new MissileBlock(-1, 2, 3, Material.AIR),
	}),
	
	BEE("Bee", Material.HONEYCOMB, ChatColor.GOLD, new MissileBlock[] {
			new MissileBlock(-1, 0, 0, Material.HONEY_BLOCK),
			new MissileBlock(0, 0, 0, Material.HONEY_BLOCK),
			new MissileBlock(1, 0, 0, Material.HONEY_BLOCK),
			new MissileBlock(-1, 0, 1, Material.HONEY_BLOCK),
			new MissileBlock(0, 0, 1, Material.HONEY_BLOCK),
			new MissileBlock(1, 0, 1, Material.HONEY_BLOCK),
			new MissileBlock(-1, 0, 2, Material.OBSERVER, 5),
			new MissileBlock(0, 0, 2, Material.HONEY_BLOCK),
			new MissileBlock(1, 0, 2, Material.OBSERVER, 5),
			new MissileBlock(-1, 0, 3, Material.PISTON, 4),
			new MissileBlock(0, 0, 3, Material.HONEY_BLOCK),
			new MissileBlock(1, 0, 3, Material.PISTON, 4),
			new MissileBlock(0, 0, 4, Material.STICKY_PISTON, 5),
			new MissileBlock(0, 0, 5, Material.OBSERVER, 4),
			new MissileBlock(0, 0, 6, Material.GLASS),
			new MissileBlock(1, 0, 4, Material.HONEY_BLOCK),
			new MissileBlock(-1, 0, 4, Material.HONEY_BLOCK),
			new MissileBlock(-1, 0, 5, Material.HONEY_BLOCK),
			new MissileBlock(1, 0, 5, Material.HONEY_BLOCK),
			new MissileBlock(-1, 1, 5, Material.TNT),
			new MissileBlock(1, 1, 5, Material.TNT),
			new MissileBlock(-1, 1, 4, Material.TNT),
			new MissileBlock(1, 1, 4, Material.TNT),
			new MissileBlock(0, 0, 6, Material.AIR),
	}),
	
	TOMAHAWK("Tomahawk", Material.RED_TERRACOTTA, ChatColor.RED, new MissileBlock[] {
			new MissileBlock(0, 0, 0, Material.PISTON, 4),
			new MissileBlock(0, 0, 1, Material.SLIME_BLOCK),
			new MissileBlock(-1, 0, 1, Material.SLIME_BLOCK),
			new MissileBlock(0, 1, 1, Material.REDSTONE_BLOCK),
			new MissileBlock(-1, 1, 1, Material.RED_TERRACOTTA),
			new MissileBlock(-1, 1, 2, Material.RED_TERRACOTTA),
			new MissileBlock(0, 0, 2, Material.STICKY_PISTON, 5),
			new MissileBlock(-1, 1, 3, Material.TNT),
			new MissileBlock(-1, 1, 4, Material.RED_TERRACOTTA),
			new MissileBlock(0, 0, 5, Material.SLIME_BLOCK),
			new MissileBlock(0, 1, 5, Material.REDSTONE_BLOCK),
			new MissileBlock(-1, 1, 5, Material.RED_TERRACOTTA),
			new MissileBlock(0, 0, 6, Material.TNT),
			new MissileBlock(-1, 1, 6, Material.TNT),
			new MissileBlock(0, 0, 7, Material.PISTON, 4),
			new MissileBlock(-1, 1, 7, Material.TNT),
			
			new MissileBlock(0, 0, 9, Material.SLIME_BLOCK),
			new MissileBlock(0, 1, 9, Material.REDSTONE_BLOCK),
			new MissileBlock(-1, 0, 9, Material.TNT),
			new MissileBlock(0, 0, 10, Material.SLIME_BLOCK),
			new MissileBlock(0, 1, 10, Material.SLIME_BLOCK),
			new MissileBlock(-1, 0, 10, Material.TNT),
			new MissileBlock(-1, 1, 10, Material.TNT),
			new MissileBlock(0, 0, 11, Material.TNT),
			new MissileBlock(0, 1, 11, Material.TNT),
			new MissileBlock(-1, 0, 11, Material.TNT),
			new MissileBlock(-1, 1, 11, Material.TNT),
			
			new MissileBlock(0, 0, 3, Material.PISTON, 4),
	}),
	
	;
	
	private final MissileEntity[] ENTITIES_EMPTY = new MissileEntity[0];
	
	private String name;
	private Material material;
	private ChatColor color;
	private MissileBlock[] blocks;
	private MissileEntity[] entities;
	
	private Missile(final String name, final Material material, final ChatColor color, final MissileBlock[] blocks) {
		this.name = name;
		this.material = material;
		this.color = color;
		this.blocks = blocks;
		this.entities = this.ENTITIES_EMPTY;
	}
	
	private Missile(final String name, final Material material, final ChatColor color, final MissileBlock[] blocks, final MissileEntity[] entities) {
		this.name = name;
		this.material = material;
		this.color = color;
		this.blocks = blocks;
		this.entities = entities;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public Material getMaterial() {
		return this.material;
	}
	
	public ItemStack getItem(final int amount) {
		return new ItemBuilder(this.material)
				.amount(amount)
				.name(this.color + "" + ChatColor.BOLD + this.name)
				.lore(ChatColor.GRAY + "Right click to place")
				.create();
	}
	
	public void build(final Location loc, final BlockFace direction) {
		final BlockFace front = direction;
		final BlockFace back = direction.getOppositeFace();
		BlockFace right;
		if (front == BlockFace.NORTH) {
			right = BlockFace.EAST;
		} else if (front == BlockFace.EAST) {
			right = BlockFace.SOUTH;
		} else if (front == BlockFace.SOUTH) {
			right = BlockFace.WEST;
		} else if (front == BlockFace.WEST) {
			right = BlockFace.NORTH;
		} else {
			throw new IllegalArgumentException(front.toString());
		}
		final BlockFace left = right.getOppositeFace();
		MissileBlock.build(this.blocks, loc.getBlock(), right, left, front, back);
		MissileEntity.spawn(this.entities, loc.getBlock(), right, left, front, back);
	}
	
	private static final Map<Material, Missile> BY_MATERIAL = new HashMap<>();
	
	static {
		for (final Missile missile : Missile.values()) {
			BY_MATERIAL.put(missile.material, missile);
		}
	}
	
	public static Missile fromMaterial(final Material material) {
		return BY_MATERIAL.get(material);
	}
	
}
