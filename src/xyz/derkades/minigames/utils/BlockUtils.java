package xyz.derkades.minigames.utils;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import xyz.derkades.minigames.Var;

@Deprecated
public class BlockUtils {

	public static boolean isSameBlock(final Block a, final Block b){
		return (a.getX() == b.getX() &&
				a.getY() == b.getY() &&
				a.getZ() == b.getZ() &&
				a.getWorld().getName().equals(b.getWorld().getName()));
	}

	@Deprecated
	public static void fillArea(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2, final Material material) {
		final int minX = Math.min(x1, x2);
		final int minY = Math.min(y1, y2);
		final int minZ = Math.min(z1, z2);
		final int maxX = Math.max(x1, x2);
		final int maxY = Math.max(y1, y2);
		final int maxZ = Math.max(z1, z2);

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					new Location(Var.WORLD, x, y, z).getBlock().setType(material);
				}
			}
		}
	}

	@Deprecated
	public static void fillArea(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2, final Material material, final Consumer<Block> runForEveryBlock) {
		final int minX = Math.min(x1, x2);
		final int minY = Math.min(y1, y2);
		final int minZ = Math.min(z1, z2);
		final int maxX = Math.max(x1, x2);
		final int maxY = Math.max(y1, y2);
		final int maxZ = Math.max(z1, z2);

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					final Block block = new Location(Var.WORLD, x, y, z).getBlock();
					block.setType(material);
					runForEveryBlock.accept(block);
				}
			}
		}
	}

	public static void fillArea(final World world, final int x1, final int y1, final int z1, final int x2, final int y2, final int z2, final Material material) {
		final int minX = Math.min(x1, x2);
		final int minY = Math.min(y1, y2);
		final int minZ = Math.min(z1, z2);
		final int maxX = Math.max(x1, x2);
		final int maxY = Math.max(y1, y2);
		final int maxZ = Math.max(z1, z2);

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					new Location(world, x, y, z).getBlock().setType(material);
				}
			}
		}
	}

	public static void fillArea(final World world, final int x1, final int y1, final int z1, final int x2, final int y2, final int z2, final Material material, final Consumer<Block> runForEveryBlock) {
		final int minX = Math.min(x1, x2);
		final int minY = Math.min(y1, y2);
		final int minZ = Math.min(z1, z2);
		final int maxX = Math.max(x1, x2);
		final int maxY = Math.max(y1, y2);
		final int maxZ = Math.max(z1, z2);

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					final Block block = new Location(world, x, y, z).getBlock();
					block.setType(material);
					runForEveryBlock.accept(block);
				}
			}
		}
	}

	public static void replaceBlocks(final Material original, final Material new_, final Block... blocks) {
		for (final Block block : blocks) {
			if (block.getType().equals(original)) {
				block.setType(new_);
			}
		}
	}

}
