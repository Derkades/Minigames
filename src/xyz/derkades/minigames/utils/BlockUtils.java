package xyz.derkades.minigames.utils;

import org.bukkit.block.Block;

public class BlockUtils {
	
	public static boolean isSameBlock(Block a, Block b){
		return (a.getX() == b.getX() &&
				a.getY() == b.getY() &&
				a.getZ() == b.getZ());
	}

}
