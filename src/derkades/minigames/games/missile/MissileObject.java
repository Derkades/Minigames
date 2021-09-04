package derkades.minigames.games.missile;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import derkades.minigames.Minigames;

public abstract class MissileObject {

	private final int lr, fb, ud;

	MissileObject(final int lr, final int ud, final int fb) {
		this.lr = lr; this.fb = fb; this.ud = ud;
	}

	abstract void place(Location location, BlockFace[] directions);

	public static void build(final MissileObject[] objects, final Location startLocation, final BlockFace direction, final Runnable onComplete) {
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
		build(objects, startLocation.getBlock(), right, left, front, back, onComplete);
	}

	private static void build(final MissileObject[] objects, final Block startLocation, final BlockFace right, final BlockFace left, final BlockFace front, final BlockFace back, final Runnable onComplete) {
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
				if (i.getValue() == objects.length) {
					this.cancel();
					if (onComplete != null) {
						onComplete.run();
					}
					return;
				}

				final MissileObject mo = objects[i.getAndIncrement()];

				Block block = startLocation;
				block = rel(block, mo.lr, right, left);
				block = rel(block, mo.ud, BlockFace.UP, BlockFace.DOWN);
				block = rel(block, mo.fb, front, back);

				mo.place(block.getLocation(), directions);

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
