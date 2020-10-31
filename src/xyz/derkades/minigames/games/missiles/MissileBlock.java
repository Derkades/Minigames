package xyz.derkades.minigames.games.missiles;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.Minigames;

public class MissileBlock {
	
	private final int lr, fb, ud;
	private final Material type;
	private final int facing;
	
	MissileBlock(final int lr, final int ud, final int fb, final Material type) {
		this.lr = lr; this.fb = fb; this.ud = ud;
		this.type = type;
		this.facing = 6;
	}
	
	MissileBlock(final int lr, final int ud, final int fb, final Material type, final int facing) {
		this.lr = lr; this.fb = fb; this.ud = ud;
		this.type = type;
		this.facing = facing;
	}
	
	public static void build(final MissileBlock[] mBlocks, final Block center, final BlockFace right, final BlockFace left, final BlockFace front, final BlockFace back) {
		final BlockFace[] directions = {
				BlockFace.DOWN,
				BlockFace.UP,
				left,
				right,
				front,
				back,
		};
		
		final MutableInt i = new MutableInt(0);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if (i.getValue() == mBlocks.length) {
					this.cancel();
					return;
				}
				
				final MissileBlock mb = mBlocks[i.getAndIncrement()];
				Block block = center;
				block = rel(block, mb.lr, right, left);
				block = rel(block, mb.ud, BlockFace.UP, BlockFace.DOWN);
				block = rel(block, mb.fb, front, back);
				
				
				block.setType(mb.type);
				if (mb.facing < 6) {
					final Directional dir = (Directional) block.getBlockData();
					dir.setFacing(directions[mb.facing]);
					block.setBlockData(dir);
				}
			}
		}.runTaskTimer(Minigames.getInstance(), 0, 1);
	}
	
	private static Block rel(Block block, final int comp, final BlockFace a, final BlockFace b) {
		for (int i = 0; i < comp; i++) {
			block = block.getRelative(a);
		}
		for (int i = 0; i > comp; i--) {
			block = block.getRelative(b);
		}
		return block;
	}
	
}
