package xyz.derkades.minigames.games.missiles;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;

public class MissileEntity {
	
	private final int lr, fb, ud;
	private final EntityType type;
	
	MissileEntity(final int lr, final int ud, final int fb, final EntityType type) {
		this.lr = lr; this.fb = fb; this.ud = ud;
		this.type = type;
	}
	
	public static void spawn(final MissileEntity[] mEntities, final Block center, final BlockFace right, final BlockFace left, final BlockFace front, final BlockFace back) {
		for (final MissileEntity me : mEntities) {
			Block block = center;
			block = MissileBlock.rel(block, me.lr, right, left);
			block = MissileBlock.rel(block, me.ud, BlockFace.UP, BlockFace.DOWN);
			block = MissileBlock.rel(block, me.fb, front, back);
			block.getLocation().getWorld().spawnEntity(block.getLocation(), me.type);
		}
	}

}
