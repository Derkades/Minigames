package derkades.minigames.games.breaktheblock;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Openable;

import derkades.minigames.Logger;
import derkades.minigames.random.Size;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.worlds.GameWorld;
import xyz.derkades.derkutils.bukkit.BlockUtils;

public class Cake extends BreakTheBlockMap {

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	Location getStartLocation() {
		return new Location(this.getWorld(), 0.5, 68, 19.5, 180, 0);
	}

	@Override
	public String getName() {
		return "Cake";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.BTB_CAKE;
	}

	@Override
	public String getCredits() {
		return "Sneewie";
	}

	@Override
	public String getIdentifier() {
		return "btb_cake";
	}

	private void placeEatSign(final int x, final int y, final int z, final BlockFace facing) {
		final Block block = this.getWorld().getBlockAt(x, y, z);
		block.setType(Material.CRIMSON_WALL_SIGN, false);
		final Sign sign = (Sign) block.getState();
		sign.setLine(1, "Eat Me!");
		sign.setGlowingText(true);
		sign.setColor(DyeColor.RED);
		sign.update();
		block.getBlockData();
		final Directional directional = (Directional) block.getBlockData();
		directional.setFacing(facing);
		block.setBlockData(directional, true);
	}

	@Override
	public void onStart() {
		try {
			BlockUtils.fillArea(this.getWorld(), 1, 94, 1, -1, 96, -1, Material.RED_GLAZED_TERRACOTTA, null, false);
			this.getWorld().getBlockAt(0, 95, 0).setType(Material.GOLD_BLOCK);
			placeEatSign(2, 95, 0, BlockFace.EAST);
			placeEatSign(0, 95, 2, BlockFace.SOUTH);
			placeEatSign(-2, 95, 0, BlockFace.WEST);
			placeEatSign(0, 95, -2, BlockFace.NORTH);
		} catch (final Exception e) {
			e.printStackTrace();
			Logger.warning("Failed to place blocks on top of cake");
		}
	}

	@Override
	public void onTimer(final int secondsLeft) {
		if (secondsLeft % 10 == 0) {
			setTrapdoorState(true);
		} else if (secondsLeft % 10 == 6) {
			setTrapdoorState(false);
		}
	}

	private void setTrapdoorState(final boolean state) {
		final Location[] trapdoors = {
				new Location(this.getWorld(), 1, 82, -1),
				new Location(this.getWorld(), 1, 83, 1),
				new Location(this.getWorld(), -1, 84, 1),
				new Location(this.getWorld(), -1, 85, -1),
				new Location(this.getWorld(), 1, 86, -1),
		};

		for (final Location loc : trapdoors) {
			final Block trapdoor = loc.getBlock();
			try {
				final Openable openable = (Openable) trapdoor.getBlockData();
				openable.setOpen(!state);
				trapdoor.setBlockData(openable);
			} catch (final ClassCastException e) {
				Logger.warning("Failed to modify state for trapdoor at %s, %s, %s", trapdoor.getX(), trapdoor.getY(), trapdoor.getZ());
			}
		}
	}

	@Override
	boolean canTakeDamage(final MPlayer player) {
		return true;
	}

}
