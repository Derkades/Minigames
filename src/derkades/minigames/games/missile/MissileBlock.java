package derkades.minigames.games.missile;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;

import derkades.minigames.Logger;

public class MissileBlock extends MissileObject {

	public static final int DOWN = 0;
	public static final int UP = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int FRONT = 4;
	public static final int BACK = 5;

	private final Material type;
	private final int facing;

	MissileBlock(final int lr, final int ud, final int fb, final Material type) {
		super(lr, ud, fb);
		this.type = type;
		this.facing = 6;
	}

	MissileBlock(final int lr, final int ud, final int fb, final Material type, final int facing) {
		super(lr, ud, fb);
		this.type = type;
		this.facing = facing;
	}

	private static Set<Material> DENY_REPLACE = Set.of(
			Material.BEDROCK, Material.OBSIDIAN, Material.BARRIER, Material.NETHER_PORTAL, Material.VOID_AIR
	);

	@Override
	void place(final Location location, final BlockFace[] directions) {
		final Block block = location.getBlock();
		if (DENY_REPLACE.contains(block.getType())) {
			Logger.debug("Skipped replacing block at (%s, %s, %s) with type %s", block.getX(), block.getY(), block.getZ(), block.getType());
			return;
		}

		block.setType(this.type);
		try {
			if (this.facing != 6) {
				final Directional dir = (Directional) block.getBlockData();
				dir.setFacing(directions[this.facing]);
				block.setBlockData(dir);
			}
		} catch (final ClassCastException e) {
			Logger.warning("Failed to set facing=%s data for block at (%s, %s, %s), (type actual=%s, expected=%s)", this.facing, block.getX(), block.getY(), block.getZ(), block.getType(), this.type);
		} catch (final IndexOutOfBoundsException e) {
			Logger.warning("Index out of bounds facing=%s when building block (%s, %s, %s) type %s", this.facing, block.getX(), block.getY(), block.getZ(), this.type);
		}
	}

}
