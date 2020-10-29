package xyz.derkades.minigames.games;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.Minigames;

public class Missiles {
	
	public static class Missile {
		
//		static Missile POCKET_TNT = new Missile((center, front, back, left, right) -> {
//			center.setType(Material.SLIME_BLOCK);
//			final Block r1 = center.getRelative(right);
//			r1.setType(Material.PISTON);
//			((Directional) r1.getBlockData()).setFacing(front);
//			r1.getRelative(front).setType(Material.SLIME_BLOCK);
//			final Block r1u1 = r1.getRelative(BlockFace.UP);
//			r1u1.getRelative(front).setType(Material.SLIME_BLOCK);
//			r1u1.getRelative(front).getRelative(BlockFace.UP).setType(Material.SLIME_BLOCK);
//			r1u1.setType(Material.OBSERVER);
//			((Directional) r1u1.getBlockData()).setFacing(BlockFace.UP);
//			center.getRelative(BlockFace.UP).setType(Material.TNT);
//			final Block f1 = center.getRelative(front);
//			f1.setType(Material.STICKY_PISTON);
//			((Directional) f1).setFacing(back);
//			final Block f1u1 = f1.getRelative(BlockFace.UP);
//		});
		
		// <-left/+right> <+up/-down> <+front/-back> <material> <facing>
		// facing = 0-down 1-up 2-left 3-right 4-front 5-back

		public static Missile TEST = new Missile(new MissileBlock[] {
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
		});
		
		public static Missile JUGGERNAUT = new Missile(new MissileBlock[] {
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
		});
		
		public static Missile COMPRESSOR = new Missile(new MissileBlock[] {
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
//				new MissileBlock(0, 1, 7, Material.TNT),

				new MissileBlock(0, 2, 3, Material.OBSERVER, 2),
				new MissileBlock(-1, 2, 3, Material.GLASS),
				new MissileBlock(-1, 2, 3, Material.AIR),
		});
		
		private final MissileBlock[] blocks;
		
		Missile(final MissileBlock[] blocks) {
			this.blocks = blocks;
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
		}
		
	}
	
	public static class MissileBlock {
		
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

}
