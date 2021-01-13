package xyz.derkades.minigames.games.missiles;

import java.util.Set;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.Logger;
import xyz.derkades.minigames.Minigames;

public class MissileBlock {
	
	static final int DOWN = 0;
	static final int UP = 1;
	static final int LEFT = 2;
	static final int RIGHT = 3;
	static final int FRONT = 4;
	static final int BACK = 5;
	
	private final int lr, fb, ud;
	private final Material type;
	private final int facing;
	
	public MissileBlock(final int lr, final int ud, final int fb, final Material type) {
		this.lr = lr; this.fb = fb; this.ud = ud;
		this.type = type;
		this.facing = 6;
	}
	
	MissileBlock(final int lr, final int ud, final int fb, final Material type, final int facing) {
		this.lr = lr; this.fb = fb; this.ud = ud;
		this.type = type;
		this.facing = facing;
	}
	
	public static void build(final MissileBlock[] mBlocks, final MissileEntity[] mEntities, final Location center, final BlockFace direction, final Runnable onComplete) {
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
		MissileBlock.build(mBlocks, center.getBlock(), right, left, front, back, () -> {
			if (mEntities != null) {
				MissileEntity.spawn(mEntities, center.getBlock(), right, left, front, back);
			}
			if (onComplete != null) {
				onComplete.run();
			}
		});
	}
	
	private static Set<Material> BLACKLIST = Set.of(
			Material.BEDROCK, Material.OBSIDIAN, Material.BARRIER
	);
	
	public static void build(final MissileBlock[] mBlocks, final Block center, final BlockFace right, final BlockFace left, final BlockFace front, final BlockFace back, final Runnable onComplete) {
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
					if (onComplete != null) {
						onComplete.run();
					}
					return;
				}
				
				final MissileBlock mb = mBlocks[i.getAndIncrement()];
				Block block = center;
				block = rel(block, mb.lr, right, left);
				block = rel(block, mb.ud, BlockFace.UP, BlockFace.DOWN);
				block = rel(block, mb.fb, front, back);
				
				if (BLACKLIST.contains(block.getType())) {
					Logger.debug("Skipped replacing block at (%s, %s, %s) with type %s", block.getX(), block.getY(), block.getZ(), block.getType());
					return;
				}
				
				block.setType(mb.type);
				if (mb.facing < 6) {
					final Directional dir = (Directional) block.getBlockData();
					dir.setFacing(directions[mb.facing]);
					block.setBlockData(dir);
				}
				
				block.getWorld().playSound(block.getLocation(), Sound.BLOCK_STONE_PLACE, 0.75f, 1.0f);
				block.getWorld().spawnParticle(Particle.WHITE_ASH, block.getLocation(), 5);
				block.getWorld().spawnParticle(Particle.ASH, block.getLocation(), 5);
			}
		}.runTaskTimer(Minigames.getInstance(), 0, 1);
	}
	
	static Block rel(Block block, final int comp, final BlockFace a, final BlockFace b) {
		for (int i = 0; i < comp; i++) {
			block = block.getRelative(a);
		}
		for (int i = 0; i > comp; i--) {
			block = block.getRelative(b);
		}
		return block;
	}
	
}
