package derkades.minigames.games.breaktheblock;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Openable;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.jetbrains.annotations.NotNull;

import derkades.minigames.Logger;
import derkades.minigames.random.Size;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.worlds.GameWorld;
import net.kyori.adventure.text.Component;
import xyz.derkades.derkutils.bukkit.BlockUtils;

public class Cake extends BreakTheBlockMap {

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	public @NotNull String getName() {
		return "Cake";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.BTB_CAKE;
	}

	@Override
	public String getCredits() {
		return "Sneewie";
	}

	@Override
	public @NotNull String getIdentifier() {
		return "btb_cake";
	}

	@Override
	Location[] getStartLocations() {
		return new Location[] {
				new Location(this.getWorld(), 0.5, 68, 19.5, 180, 0),
				new Location(this.getWorld(), 0.5, 69, -18.5, 0, 0),
		};
	}

	private void placeEatSign(final int x, final int y, final int z, final BlockFace facing) {
		final Block block = this.getWorld().getBlockAt(x, y, z);
		block.setType(Material.CRIMSON_WALL_SIGN, false);
		final Sign sign = (Sign) block.getState();
		final SignSide side = sign.getSide(Side.FRONT);
		side.line(1, Component.text("Eat Me!"));
		side.setGlowingText(true);
		side.setColor(DyeColor.RED);
		sign.update();
		block.getBlockData();
		final Directional directional = (Directional) block.getBlockData();
		directional.setFacing(facing);
		block.setBlockData(directional, true);
	}

	@Override
	public void onPreStart() {
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

		BlockUtils.fillArea(this.getWorld(), -1, 69, 17, 1, 69, 17, Material.COBBLED_DEEPSLATE_WALL);
		BlockUtils.fillArea(this.getWorld(), -1, 69, -17, 1, 69, -17, Material.COBBLED_DEEPSLATE_WALL);
	}

	@Override
	public void onStart() {
		BlockUtils.fillArea(this.getWorld(), -1, 69, 17, 1, 69, 17, Material.AIR);
		BlockUtils.fillArea(this.getWorld(), -1, 69, -17, 1, 69, -17, Material.AIR);
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
