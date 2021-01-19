package xyz.derkades.minigames.games.missiles;

import static xyz.derkades.minigames.games.missiles.MissileBlock.BACK;
import static xyz.derkades.minigames.games.missiles.MissileBlock.DOWN;
import static xyz.derkades.minigames.games.missiles.MissileBlock.FRONT;
import static xyz.derkades.minigames.games.missiles.MissileBlock.UP;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import xyz.derkades.derkutils.bukkit.ItemBuilder;

public enum Missile {
	
	// <-left/+right> <+up/-down> <+front/-back> <material> <facing>

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
			new MissileBlock(1, 0, 5, Material.SLIME_BLOCK),
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
	
	HYPER_COMPRESSOR("Hypercompressor", Material.PISTON, ChatColor.GRAY, new MissileBlock[] {
			new MissileBlock(-1, 0, 1, Material.SLIME_BLOCK),
			new MissileBlock(-1, -1, 1, Material.OBSERVER, DOWN),
			new MissileBlock(0, 0, 1, Material.PISTON, FRONT),
			new MissileBlock(-1, 0, 2, Material.SLIME_BLOCK),
			new MissileBlock(0, 0, 2, Material.WHITE_GLAZED_TERRACOTTA),
			new MissileBlock(0, -1, 2, Material.SLIME_BLOCK),
			new MissileBlock(1, -1, 2, Material.TNT),
			new MissileBlock(0, 0, 3, Material.SLIME_BLOCK),
			new MissileBlock(0, -1, 3, Material.STICKY_PISTON, BACK),
			new MissileBlock(1, -1, 3, Material.TNT),
			new MissileBlock(1, 0, 3, Material.PISTON, FRONT),
			new MissileBlock(-1, 0, 3, Material.STICKY_PISTON, BACK),
			new MissileBlock(0, 1, 3, Material.OBSERVER, UP),
			new MissileBlock(0, 0, 4, Material.PISTON, FRONT),
			new MissileBlock(1, -1, 4, Material.TNT),
			new MissileBlock(-1, 0, 4, Material.TNT),
			new MissileBlock(1, 0, 5, Material.TNT),
			new MissileBlock(1, -1, 5, Material.TNT),
			new MissileBlock(-1, 0, 5 , Material.OBSERVER, BACK),
			new MissileBlock(0, 0, 6, Material.TNT),
			new MissileBlock(1, 0, 6, Material.TNT),
			new MissileBlock(-1, 0, 6 , Material.PISTON, FRONT),
			new MissileBlock(1, -1, 6, Material.TNT),
			new MissileBlock(0, 0, 7, Material.TNT),
			new MissileBlock(1, 0, 7, Material.TNT),
			new MissileBlock(1, -1, 7, Material.TNT),
			new MissileBlock(-1, 0, 8, Material.TNT),
			new MissileBlock(0, 0, 8, Material.TNT),
			new MissileBlock(1, 0, 8, Material.TNT),
			new MissileBlock(1, -1, 8, Material.TNT),
			new MissileBlock(-1, 0, 9, Material.TNT),
			new MissileBlock(0, 0, 9, Material.TNT),
			new MissileBlock(1, 0, 9, Material.TNT),
			new MissileBlock(1, -1, 9, Material.TNT),
			new MissileBlock(-1, 0, 10, Material.TNT),
			new MissileBlock(0, 0, 10, Material.TNT),
			new MissileBlock(1, 0, 10, Material.TNT),
			new MissileBlock(1, -1, 10, Material.TNT),
			new MissileBlock(-1, 0, 11, Material.TNT),
			new MissileBlock(1, 0, 11, Material.TNT),
			new MissileBlock(1, -1, 11, Material.SLIME_BLOCK),
			new MissileBlock(0, -1, 11, Material.PISTON, FRONT),
			new MissileBlock(-1, -1, 13, Material.TNT),
			new MissileBlock(1, -1, 13, Material.TNT),
			new MissileBlock(0, 0, 13, Material.REDSTONE_BLOCK),
			new MissileBlock(0, -1, 13, Material.SLIME_BLOCK),
			new MissileBlock(-1, -1, 14, Material.TNT),
			new MissileBlock(1, -1, 14, Material.TNT),
			new MissileBlock(0, 0, 14, Material.SLIME_BLOCK),
			new MissileBlock(0, -1, 14, Material.SLIME_BLOCK),
			new MissileBlock(-1, 0, 14, Material.TNT),
			new MissileBlock(1, 0, 14, Material.TNT),
			new MissileBlock(0, 0, 15, Material.TNT),
			new MissileBlock(0, -1, 15, Material.TNT),
			new MissileBlock(-1, -2, 1, Material.GLASS),
			new MissileBlock(-1, -2, 1, Material.AIR),
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
//			new MissileBlock(-1, 1, 5, Material.TNT),
//			new MissileBlock(1, 1, 5, Material.TNT),
//			new MissileBlock(-1, 1, 4, Material.TNT),
//			new MissileBlock(1, 1, 4, Material.TNT),
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
	
	MINECART("Minecart", Material.TNT_MINECART, ChatColor.GRAY, new MissileBlock[] {
			new MissileBlock(0, 0, 0, Material.PISTON, 4),
			new MissileBlock(0, 1, 0, Material.PISTON, 5),
			new MissileBlock(-1, 0, 0, Material.SLIME_BLOCK),
			new MissileBlock(-1, 1, 0, Material.SLIME_BLOCK),
			new MissileBlock(-1, 1, 1, Material.SLIME_BLOCK),
			new MissileBlock(0, 0, 2, Material.SLIME_BLOCK),
			new MissileBlock(0, 1, 2, Material.REDSTONE_BLOCK),
			new MissileBlock(0, 1, 3, Material.SLIME_BLOCK),
			new MissileBlock(-1, 1, 3, Material.STICKY_PISTON, 5),
			new MissileBlock(0, 1, 4, Material.GLASS),
			new MissileBlock(-1, 1, 4, Material.REDSTONE_BLOCK),
			new MissileBlock(0, 0, 3, Material.TNT),
			new MissileBlock(0, 0, 4, Material.TNT),
			new MissileBlock(-1, 0, 4, Material.SLIME_BLOCK),
			new MissileBlock(-1, 0, 3, Material.PISTON, 4),
			
			new MissileBlock(0, 1, 5, Material.GLASS),
			new MissileBlock(-1, 1, 5, Material.GLASS),
			new MissileBlock(0, 0, 5, Material.TNT),
			new MissileBlock(-1, 0, 5, Material.TNT),
			
			new MissileBlock(0, 1, 6, Material.STRIPPED_ACACIA_LOG, 4),
			new MissileBlock(-1, 1, 6, Material.PISTON, 2),
			new MissileBlock(-1, 0, 6, Material.PISTON, 4),
			
			new MissileBlock(0, 0, 7, Material.ANDESITE_WALL),
			
			new MissileBlock(0, 0, 8, Material.SLIME_BLOCK),
			new MissileBlock(-1, 0, 8, Material.SLIME_BLOCK),
			new MissileBlock(-1, 1, 8, Material.REDSTONE_BLOCK),
			
			new MissileBlock(0, 0, 9, Material.TNT),
			new MissileBlock(-1, 0, 9, Material.TNT),

			new MissileBlock(-1, 1, 9, Material.RAIL),
			new MissileBlock(0, 1, 9, Material.RAIL),
			new MissileBlock(0, 1, 8, Material.POWERED_RAIL),
			
			new MissileBlock(0, 0, 10, Material.TNT),
			new MissileBlock(-1, 0, 10, Material.PISTON, 4),
			new MissileBlock(-1, 1, 10, Material.PISTON, 3),
			
			new MissileBlock(0, 0, 12, Material.TNT),
			new MissileBlock(-1, 0, 12, Material.SLIME_BLOCK),
			new MissileBlock(-1, 1, 12, Material.REDSTONE_BLOCK),
			
			new MissileBlock(0, 0, 13, Material.TNT),
			new MissileBlock(0, 1, 13, Material.TNT),
			new MissileBlock(-1, 0, 13, Material.TNT),
			new MissileBlock(-1, 1, 13, Material.SLIME_BLOCK),
			
			new MissileBlock(0, 0, 14, Material.TNT),
			new MissileBlock(0, 1, 14, Material.TNT),
			new MissileBlock(-1, 0, 14, Material.TNT),
			new MissileBlock(-1, 1, 14, Material.TNT),
			
			new MissileBlock(-1, 0, 2, Material.TNT),
	}, new MissileEntity[] {
			new MissileEntity(0, 1, 8, EntityType.MINECART_TNT),
	}),
	
	SHIELDBUSTER_MINI("Shieldbuster Mini", Material.PISTON, ChatColor.GRAY, new MissileBlock[] {
			new MissileBlock(-1, 0, 0, Material.LIME_CONCRETE),
			new MissileBlock(0, 0, 0, Material.HONEY_BLOCK),
			new MissileBlock(1, 0, 0, Material.LIME_CONCRETE),
			new MissileBlock(-1, 0, 1, Material.OBSERVER, 5),
			new MissileBlock(0, 0, 1, Material.HONEY_BLOCK),
			new MissileBlock(1, 0, 1, Material.OBSERVER, 5),
			new MissileBlock(-1, 0, 2, Material.PISTON, 4),
			new MissileBlock(0, 0, 2, Material.HONEY_BLOCK),
			new MissileBlock(1, 0, 2, Material.PISTON, 4),
			new MissileBlock(-1, 0, 4, Material.SLIME_BLOCK),
			new MissileBlock(0, 0, 4, Material.STICKY_PISTON, 5),
			new MissileBlock(1, 0, 4, Material.HONEY_BLOCK),
			new MissileBlock(-1, 1, 4, Material.SLIME_BLOCK),
			new MissileBlock(0, 1, 4, Material.SLIME_BLOCK),
			new MissileBlock(1, 1, 4, Material.HONEY_BLOCK),
			new MissileBlock(-1, 2, 4, Material.SLIME_BLOCK),
			new MissileBlock(0, 2, 4, Material.HONEY_BLOCK),
			new MissileBlock(1, 2, 4, Material.HONEY_BLOCK),
			new MissileBlock(0, 0, 6, Material.GLASS),
			new MissileBlock(0, 0, 5, Material.OBSERVER, 4),
			new MissileBlock(0, 0, 6, Material.AIR),
	}),
	
	GUARDIAN("Guardian", Material.SHIELD, ChatColor.DARK_GRAY, new MissileBlock[] {
			new MissileBlock(0, 0, 0, Material.OBSERVER, 5),
			
			new MissileBlock(0, 0, 1, Material.SLIME_BLOCK),
			
			new MissileBlock(-1, 0, 1, Material.PISTON, 4),
			new MissileBlock(1, 0, 1, Material.PISTON, 4),
			new MissileBlock(0, 1, 1, Material.PISTON, 4),
			new MissileBlock(0, -1, 1, Material.PISTON, 4),
			
			new MissileBlock(0, 1, 4, Material.OBSERVER, 4),
			
			new MissileBlock(-1, 1, 3, Material.PISTON, 4),
			new MissileBlock(1, 1, 3, Material.PISTON, 4),
			new MissileBlock(0, 2, 3, Material.PISTON, 4),
			new MissileBlock(0, 0, 3, Material.STICKY_PISTON, 5),
			new MissileBlock(0, 1, 3, Material.SLIME_BLOCK, 5),
			
			new MissileBlock(0, 0, 4, Material.TNT),
			
			new MissileBlock(0, 0, 5, Material.OBSERVER, 5),
			
			new MissileBlock(0, 1, 6, Material.OBSERVER, 5),
			new MissileBlock(0, 0, 6, Material.PISTON, 4),
			
			new MissileBlock(0, 1, 7, Material.PISTON, 4),
			
			new MissileBlock(0, 0, 8, Material.TNT),
			new MissileBlock(0, 0, 9, Material.TNT),
			new MissileBlock(0, 1, 9, Material.TNT),
			new MissileBlock(0, 0, 10, Material.TNT),
			new MissileBlock(0, 1, 10, Material.TNT),
			new MissileBlock(0, 0, 11, Material.TNT),
			new MissileBlock(0, 1, 11, Material.TNT),
			new MissileBlock(0, 1, 12, Material.TNT),
			new MissileBlock(0, 1, 13, Material.PISTON, 4),
			new MissileBlock(0, 0, 14, Material.REDSTONE_BLOCK),
			new MissileBlock(0, 0, 15, Material.SLIME_BLOCK),
			new MissileBlock(0, 1, 15, Material.SLIME_BLOCK),
			
			new MissileBlock(-1, 0, 15, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, 1, 15, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 0, 15, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 1, 15, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, -1, 15, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, 2, 15, Material.BLACK_STAINED_GLASS),
			
			new MissileBlock(-1, 0, 13, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, 1, 13, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 0, 13, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 1, 13, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, -1, 13, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, 2, 13, Material.BLACK_STAINED_GLASS),
			
			new MissileBlock(-1, 0, 12, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, 1, 12, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 0, 12, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 1, 12, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, -1, 12, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, 2, 12, Material.BLACK_STAINED_GLASS),
			
			new MissileBlock(-1, 0, 11, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, 1, 11, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 0, 11, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 1, 11, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, -1, 11, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, 2, 11, Material.BLACK_STAINED_GLASS),
			
			new MissileBlock(-1, 0, 10, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, 1, 10, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 0, 10, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 1, 10, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, -1, 10, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, 2, 10, Material.BLACK_STAINED_GLASS),
			
			new MissileBlock(-1, 0, 10, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, 1, 10, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 0, 10, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 1, 10, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, -1, 10, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, 2, 10, Material.BLACK_STAINED_GLASS),
			
			new MissileBlock(-1, 0, 8, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, 1, 8, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 0, 8, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 1, 8, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, -1, 8, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, 2, 8, Material.BLACK_STAINED_GLASS),
			
			new MissileBlock(-1, 0, 7, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, 1, 7, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 0, 7, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 1, 7, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, -1, 7, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, 2, 7, Material.BLACK_STAINED_GLASS),
			
			new MissileBlock(-1, 0, 6, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, 1, 6, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 0, 6, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 1, 6, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, -1, 6, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, 2, 6, Material.BLACK_STAINED_GLASS),
			
			new MissileBlock(-1, 0, 5, Material.BLACK_STAINED_GLASS),
			new MissileBlock(-1, 1, 5, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 0, 5, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 1, 5, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, -1, 5, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, 2, 5, Material.BLACK_STAINED_GLASS),
			
			new MissileBlock(-1, 0, 4, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 0, 4, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, -1, 4, Material.BLACK_STAINED_GLASS),
	
			new MissileBlock(-1, 0, 3, Material.BLACK_STAINED_GLASS),
			new MissileBlock(1, 0, 3, Material.BLACK_STAINED_GLASS),
			new MissileBlock(0, -1, 3, Material.BLACK_STAINED_GLASS),
	
			new MissileBlock(0, 1, 5, Material.TNT),
	}),
	
	MATIGE_MISSILE_2("MatigeMissile 2", Material.SPONGE, ChatColor.LIGHT_PURPLE, new MissileBlock[] {
			new MissileBlock(0, 0, 0, Material.HONEY_BLOCK),
			new MissileBlock(-1, 0, 1, Material.HONEY_BLOCK),
			new MissileBlock(0, 0, 1, Material.HONEY_BLOCK),
			new MissileBlock(1, 0, 1, Material.HONEY_BLOCK),
			new MissileBlock(-1, 0, 2, Material.OBSERVER, BACK),
			new MissileBlock(0, 0, 2, Material.HONEY_BLOCK),
			new MissileBlock(1, 0, 2, Material.HONEY_BLOCK),
			new MissileBlock(1, 1, 2, Material.TNT),
			new MissileBlock(-1, 0, 3, Material.PISTON, FRONT),
			new MissileBlock(0, 0, 3, Material.HONEY_BLOCK),
			new MissileBlock(1, 0, 3, Material.OBSERVER, DOWN),
			new MissileBlock(1, 1, 3, Material.PISTON, FRONT),
			
			new MissileBlock(-1, 0, 5, Material.HONEY_BLOCK),
			new MissileBlock(0, 0, 5, Material.STICKY_PISTON, BACK),
			new MissileBlock(0, 1, 5, Material.TNT),
			new MissileBlock(1, 1, 5, Material.HONEY_BLOCK),
			
			new MissileBlock(-1, 0, 6, Material.HONEY_BLOCK),
			new MissileBlock(0, 0, 6, Material.OBSERVER, FRONT),
			new MissileBlock(1, 1, 6, Material.HONEY_BLOCK),
			
			new MissileBlock(-1, 0, 7, Material.PISTON, FRONT),
			new MissileBlock(1, 1, 7, Material.TNT),
			
			new MissileBlock(-1, 1, 9, Material.REDSTONE_BLOCK),
			new MissileBlock(-1, 0, 9, Material.HONEY_BLOCK),
			new MissileBlock(0, 0, 9, Material.HONEY_BLOCK),
			new MissileBlock(1, 0, 9, Material.REDSTONE_BLOCK),
			
			new MissileBlock(0, 0, 7, Material.GLASS),
			new MissileBlock(0, 0, 7, Material.AIR),
	}),
	
	MINECART_2("Minecart 2", Material.TNT_MINECART, ChatColor.LIGHT_PURPLE, new MissileBlock[] {
			new MissileBlock(0, 0, 0, Material.TNT),
			new MissileBlock(0, -1, 0, Material.TNT),
			new MissileBlock(1, -1, 0, Material.TNT),
			new MissileBlock(1, 0, 0, Material.SLIME_BLOCK),
			new MissileBlock(1, 1, 0, Material.TNT),
			
			new MissileBlock(0, 0, 1, Material.SLIME_BLOCK),
			new MissileBlock(0, -1, 1, Material.SLIME_BLOCK),
			new MissileBlock(1, -1, 1, Material.PISTON, FRONT),
			new MissileBlock(1, 0, 1, Material.STICKY_PISTON, BACK),
			new MissileBlock(1, 1, 1, Material.TNT),
			new MissileBlock(0, 1, 1, Material.TNT),
			
			new MissileBlock(0, 0, 2, Material.SLIME_BLOCK),
			new MissileBlock(0, 1, 2, Material.TNT),
			new MissileBlock(0, -1, 2, Material.TNT),
			new MissileBlock(1, 1, 2, Material.WHITE_GLAZED_TERRACOTTA),
			
			new MissileBlock(0, 1, 3, Material.TNT),
			new MissileBlock(1, 1, 3, Material.WHITE_GLAZED_TERRACOTTA),
			new MissileBlock(1, 0, 3, Material.REDSTONE_BLOCK),
			new MissileBlock(1, -1, 3, Material.SLIME_BLOCK),
			
			new MissileBlock(0, -1, 4, Material.PISTON, FRONT),
			new MissileBlock(0, 0, 4, Material.STICKY_PISTON, BACK),
			new MissileBlock(1, 1, 4, Material.WHITE_GLAZED_TERRACOTTA),
			new MissileBlock(1, 0, 4, Material.SLIME_BLOCK),
			new MissileBlock(0, 1, 4, Material.TNT),
			
			new MissileBlock(1, 1, 5, Material.WHITE_GLAZED_TERRACOTTA),
			
			new MissileBlock(1, 0, 6, Material.GRANITE_WALL),
			new MissileBlock(0, 0, 6, Material.REDSTONE_BLOCK),
			new MissileBlock(0, -1, 6, Material.SLIME_BLOCK),
			
			new MissileBlock(1, 0, 7, Material.SLIME_BLOCK),
			new MissileBlock(0, 0, 7, Material.SLIME_BLOCK),
			new MissileBlock(0, 1, 7, Material.REDSTONE_BLOCK),
			
			new MissileBlock(1, 0, 8, Material.TNT),
			new MissileBlock(0, 0, 8, Material.TNT),
			
			new MissileBlock(1, 1, 7, Material.POWERED_RAIL),
			new MissileBlock(1, 1, 8, Material.RAIL),
			new MissileBlock(0, 1, 8, Material.RAIL),
			
			new MissileBlock(-1, 0, 4, Material.REDSTONE_BLOCK),
			new MissileBlock(1, 1, 9, Material.TNT),
			new MissileBlock(-1, 0, 4, Material.AIR),
			
	}, new MissileEntity[] {
			new MissileEntity(1, 2, 7, EntityType.MINECART_TNT),
	}),
	
	TIJDBOM("Tijdbom", Material.CLOCK, ChatColor.BLUE, new MissileBlock[] {
			new MissileBlock(0, 0, 0, Material.TNT),
			new MissileBlock(-1, 0, 0, Material.SLIME_BLOCK),
			new MissileBlock(-1, 1, 0, Material.OBSERVER, 3),
			new MissileBlock(-2, 0, 0, Material.PISTON, 4),

			new MissileBlock(-1, 1, 2, Material.OBSERVER, 4),

			new MissileBlock(0, 0, 1, Material.TNT),
			new MissileBlock(-1, 0, 1, Material.STICKY_PISTON, 5),
			new MissileBlock(-2, 0, 1, Material.WHITE_GLAZED_TERRACOTTA),

			new MissileBlock(0, 0, 2, Material.TNT ),
			new MissileBlock(-1, 0, 2, Material.SLIME_BLOCK),
			new MissileBlock(-2, 0, 2, Material.GLASS),

			new MissileBlock(0, 0, 3, Material.TNT ),
			new MissileBlock(-3, 0, 3, Material.OBSERVER, 2),
			new MissileBlock(-1, 0, 3, Material.STICKY_PISTON, 5),
			new MissileBlock(-2, 0, 3, Material.SLIME_BLOCK),
			new MissileBlock(-2, 1, 3, Material.PISTON, 4),
			new MissileBlock(-2, -1, 3, Material.PISTON, 4),

			new MissileBlock(0, 0, 4, Material.TNT),
			new MissileBlock(-2, 0, 4, Material.PISTON, 4),

			new MissileBlock(0, 0, 5, Material.TNT),
			new MissileBlock(-1, 1, 5, Material.TNT),
			new MissileBlock(-1, -1, 5, Material.TNT),
			new MissileBlock(-2, 1, 5, Material.WHITE_GLAZED_TERRACOTTA),
			new MissileBlock(-2, -1, 5, Material.WHITE_GLAZED_TERRACOTTA),

			new MissileBlock(0, 0, 6, Material.TNT ),
			new MissileBlock(0, -1, 6, Material.TNT),
			new MissileBlock(-1, -1, 6, Material.SLIME_BLOCK),
			new MissileBlock(-2, -1, 6, Material.OBSERVER, 5),
			new MissileBlock(-2, 0, 6, Material.TNT),
			new MissileBlock(0, 1, 6, Material.TNT),
			new MissileBlock(-1, 1, 6, Material.SLIME_BLOCK),
			new MissileBlock(-2, 1, 6, Material.OBSERVER, 5),

			new MissileBlock(0, 0, 7, Material.TNT),
			new MissileBlock(0, -1, 7, Material.TNT),
			new MissileBlock(-1, -1, 7, Material.STICKY_PISTON, 5),
			new MissileBlock(-2, -1, 7, Material.SLIME_BLOCK),
			new MissileBlock(-3, -1, 7, Material.PISTON, 4),
			new MissileBlock(-2, 0, 7, Material.WHITE_GLAZED_TERRACOTTA),
			new MissileBlock(0, 1, 7, Material.TNT),
			new MissileBlock(-1, 1, 7, Material.STICKY_PISTON, 5),
			new MissileBlock(-2, 1, 7, Material.SLIME_BLOCK),
			new MissileBlock(-3, 1, 7, Material.PISTON, 4),

			new MissileBlock(0, 0, 8, Material.TNT),
			new MissileBlock(0, -1, 8, Material.OBSERVER, 5),
			new MissileBlock(0, 1, 8, Material.OBSERVER, 5),
			new MissileBlock(-2, 0, 8, Material.TNT),
			new MissileBlock(-2, -1, 8, Material.PISTON, 4),
			new MissileBlock(-2, 1, 8, Material.PISTON, 4),

			new MissileBlock(0, 0, 9, Material.TNT),
			new MissileBlock(0, -1, 9, Material.PISTON, 2),
			new MissileBlock(0, 1, 9, Material.PISTON, 2),
			new MissileBlock(-2, -1, 9, Material.TNT),
			new MissileBlock(-3, -1, 9, Material.TNT),
			new MissileBlock(-2, 0, 9, Material.GLASS),
			new MissileBlock(-2, 1, 9, Material.GLASS),
			new MissileBlock(-3, 1, 9, Material.TNT),

			new MissileBlock(0, -1, 10, Material.TNT),
			new MissileBlock(-1, -1, 10, Material.TNT),
			new MissileBlock(-2, -1, 10, Material.TNT),
			new MissileBlock(-3, -1, 10, Material.TNT),

			new MissileBlock(0, -1, 11, Material.TNT),
			new MissileBlock(-1, -1, 11, Material.TNT),
			new MissileBlock(-2, -1, 11, Material.OBSERVER, 0),
			new MissileBlock(-3, -1, 11, Material.TNT),

			new MissileBlock(0, -1, 12, Material.TNT),
			new MissileBlock(-1, -1, 12, Material.TNT),
			new MissileBlock(-2, -1, 12, Material.TNT),
			new MissileBlock(-3, -1, 12, Material.TNT),

			new MissileBlock(0, -1, 13, Material.TNT),
			new MissileBlock(-1, -1, 13, Material.TNT),
			new MissileBlock(-2, -1, 13, Material.TNT),
			new MissileBlock(-3, -1, 13, Material.TNT),

			new MissileBlock(0, -1, 14, Material.TNT),
			new MissileBlock(-1, -1, 14, Material.TNT),
			new MissileBlock(-2, -1, 14, Material.TNT),
			new MissileBlock(-3, -1, 14, Material.OBSERVER, 5),

			new MissileBlock(0, -1, 15, Material.TNT),
			new MissileBlock(-1, -1, 15, Material.TNT),
			new MissileBlock(-3, -1, 15, Material.PISTON, 3),

			new MissileBlock(0, 0, 10, Material.TNT),
			new MissileBlock(-2, 0, 10, Material.STONE),

			new MissileBlock(0, 1, 10, Material.TNT),
			new MissileBlock(-1, 1, 10, Material.TNT),
			new MissileBlock(-2, 1, 10, Material.TNT),
			new MissileBlock(-3, 1, 10, Material.TNT),

			new MissileBlock(0, 1, 11, Material.TNT),
			new MissileBlock(-1, 1, 11, Material.GLASS),
			new MissileBlock(-2, 1, 11, Material.TNT),
			new MissileBlock(-3, 1, 11, Material.TNT),

			new MissileBlock(0, 1, 12, Material.TNT),
			new MissileBlock(-1, 1, 12, Material.TNT),
			new MissileBlock(-2, 1, 12, Material.TNT),
			new MissileBlock(-3, 1, 12, Material.TNT),

			new MissileBlock(0, 1, 13, Material.TNT),
			new MissileBlock(-1, 1, 13, Material.GLASS),
			new MissileBlock(-2, 1, 13, Material.GLASS),
			new MissileBlock(-3, 1, 13, Material.OBSERVER, 5),

			new MissileBlock(0, 1, 14, Material.TNT),
			new MissileBlock(-1, 1, 14, Material.TNT),
			new MissileBlock(-3, 1, 14, Material.PISTON, 3),

			new MissileBlock(0, 1, 15, Material.TNT),

			new MissileBlock(0, 1, 0, Material.GLASS),
			new MissileBlock(0, 1, 0, Material.AIR),
	}),
	
	PLATVIS("Platvis", Material.SALMON, ChatColor.BLUE, new MissileBlock[] {
			new MissileBlock(2, 0, 0, Material.OBSERVER, 5),
			new MissileBlock(1, 0, 1, Material.OBSERVER, 5),
			new MissileBlock(2, 0, 1, Material.SLIME_BLOCK),
			new MissileBlock(3, 0, 1, Material.PISTON, 4),
			new MissileBlock(1, 0, 2, Material.PISTON, 4),
			new MissileBlock(2, 0, 3, Material.STICKY_PISTON, 5),
			new MissileBlock(3, 0, 3, Material.SLIME_BLOCK),
			new MissileBlock(4, 0, 3, Material.TNT),
			new MissileBlock(1, 0, 4, Material.TNT),
			new MissileBlock(2, 0, 4, Material.OBSERVER, 4),
			new MissileBlock(3, 0, 4, Material.OBSERVER, 5),
			new MissileBlock(4, 0, 4, Material.TNT),
			new MissileBlock(1, 0, 5, Material.TNT),
			new MissileBlock(3, 0, 5, Material.PISTON, 4),
			new MissileBlock(4, 0, 5, Material.TNT),
			new MissileBlock(1, 0, 6, Material.PISTON, 1),
			new MissileBlock(1, 0, 7, Material.TNT),
			new MissileBlock(2, 0, 7, Material.SLIME_BLOCK),
			new MissileBlock(3, 0, 7, Material.SLIME_BLOCK),
			new MissileBlock(4, 0, 7, Material.PISTON, 4),
			new MissileBlock(1, 0, 8, Material.GLASS),
			new MissileBlock(2, 0, 8, Material.SLIME_BLOCK),
			new MissileBlock(3, 0, 8, Material.OBSERVER, 4),
			new MissileBlock(0, 0, 9, Material.REDSTONE_BLOCK),
			new MissileBlock(1, 0, 9, Material.GREEN_GLAZED_TERRACOTTA),
			new MissileBlock(2, 0, 9, Material.SLIME_BLOCK),
			new MissileBlock(4, 0, 9, Material.TNT),
			new MissileBlock(0, 0, 10, Material.HONEY_BLOCK),
			new MissileBlock(1, 0, 10, Material.SLIME_BLOCK),
			new MissileBlock(2, 0, 10, Material.SLIME_BLOCK),
			new MissileBlock(4, 0, 10, Material.OBSERVER, 5),
			new MissileBlock(0, 0, 11, Material.HONEY_BLOCK),
			new MissileBlock(4, 0, 11, Material.PISTON, 4),
			new MissileBlock(0, 0, 12, Material.HONEY_BLOCK),
			new MissileBlock(3, 0, 12, Material.TNT),
			new MissileBlock(0, 0, 13, Material.HONEY_BLOCK),
			new MissileBlock(1, 0, 13, Material.HONEY_BLOCK),
			new MissileBlock(2, 0, 13, Material.HONEY_BLOCK),
			new MissileBlock(3, 0, 13, Material.HONEY_BLOCK),
			new MissileBlock(4, 0, 13, Material.HONEY_BLOCK),
			new MissileBlock(2, 0, 5, Material.TNT),
	}),
	
	SHIELDBUSTER("Shieldbuster", Material.PISTON, ChatColor.BLUE, new MissileBlock[] {
			new MissileBlock(0, 0, 1, Material.OBSERVER, 0),
			new MissileBlock(0, 0, 2, Material.TNT),
			new MissileBlock(0, 0, 4, Material.TNT),
			new MissileBlock(0, 0, 5, Material.TNT),
			new MissileBlock(0, 0, 8, Material.OBSERVER, 5),
			new MissileBlock(0, 0, 9, Material.PISTON, 4),

			new MissileBlock(0, 1, 0, Material.PISTON, 4),
			new MissileBlock(0, 1, 1, Material.SLIME_BLOCK),
			new MissileBlock(1, 1, 1, Material.GLASS),
			new MissileBlock(-1, 1, 1, Material.GLASS),
			new MissileBlock(0, 1, 2, Material.PISTON, 4),
			new MissileBlock(1, 1, 2, Material.TNT),
			new MissileBlock(-1, 1, 2, Material.TNT),
			new MissileBlock(0, 1, 4, Material.SLIME_BLOCK),
			new MissileBlock(1, 1, 4, Material.TNT),
			new MissileBlock(-1, 1, 4, Material.TNT),
			new MissileBlock(0, 1, 5, Material.OBSERVER, 5),
			new MissileBlock(1, 1, 5, Material.TNT),
			new MissileBlock(-1, 1, 5, Material.TNT),
			new MissileBlock(0, 1, 6, Material.PISTON, 4),
			new MissileBlock(1, 1, 6, Material.TNT),
			new MissileBlock(-1, 1, 6, Material.TNT),
			new MissileBlock(0, 1, 8, Material.SLIME_BLOCK),
			new MissileBlock(1, 1, 8, Material.PISTON, 4),
			new MissileBlock(-1, 1, 8, Material.PISTON, 4),
			new MissileBlock(0, 1, 9, Material.PISTON, 4),

			new MissileBlock(0, 2, 0, Material.SLIME_BLOCK),
			new MissileBlock(1, 2, 0, Material.PISTON, 4),
			new MissileBlock(-1, 2, 0, Material.PISTON, 4),
			new MissileBlock(0, 2, 1, Material.STICKY_PISTON, 5),
			new MissileBlock(0, 2, 2, Material.TNT),
			new MissileBlock(1, 2, 2, Material.TNT),
			new MissileBlock(-1, 2, 2, Material.TNT),
			new MissileBlock(1, 2, 3, Material.TNT),
			new MissileBlock(-1, 2, 3, Material.TNT),
			new MissileBlock(0, 2, 4, Material.TNT),
			new MissileBlock(1, 2, 4, Material.TNT),
			new MissileBlock(-1, 2, 4, Material.TNT),
			new MissileBlock(1, 2, 5, Material.TNT),
			new MissileBlock(-1, 2, 5, Material.TNT),
			new MissileBlock(1, 2, 6, Material.TNT),
			new MissileBlock(-1, 2, 6, Material.TNT),
			new MissileBlock(0, 2, 7, Material.OBSERVER, 5),
			new MissileBlock(1, 2, 7, Material.TNT),
			new MissileBlock(-1, 2, 7, Material.TNT),
			new MissileBlock(0, 2, 8, Material.SLIME_BLOCK),
			new MissileBlock(1, 2, 8, Material.PISTON, 4),
			new MissileBlock(-1, 2, 8, Material.PISTON, 4),
			new MissileBlock(0, 2, 9, Material.PISTON, 4),

			new MissileBlock(0, 3, 0, Material.OBSERVER, 1),
			new MissileBlock(0, 3, 1, Material.TNT),
			new MissileBlock(0, 3, 2, Material.TNT),
			new MissileBlock(0, 3, 3, Material.OBSERVER, 1),
			new MissileBlock(0, 3, 4, Material.TNT),
			new MissileBlock(0, 3, 5, Material.TNT),
			new MissileBlock(0, 3, 8, Material.PISTON, 4),
			
			new MissileBlock(0, 4, 0, Material.SPONGE),
			new MissileBlock(0, 4, 0, Material.AIR),
	});
	
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
		MissileBlock.build(this.blocks, this.entities, loc, direction, null);
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
