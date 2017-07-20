package xyz.derkades.minigames.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import xyz.derkades.minigames.Var;

public class BlockUtils {
	
	public static boolean isSameBlock(Block a, Block b){
		return (a.getX() == b.getX() &&
				a.getY() == b.getY() &&
				a.getZ() == b.getZ());
	}
	
	public static void fillArea(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Material material) {
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					new Location(Var.WORLD, x, y, z).getBlock().setType(material);
				}
			}
		}
	}

}
