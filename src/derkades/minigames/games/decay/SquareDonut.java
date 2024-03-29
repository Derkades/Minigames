package derkades.minigames.games.decay;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class SquareDonut extends DecayMap {

	@Override
	public Size getSize() {
		return Size.NORMAL;
	}

	@Override
	public @NotNull String getName() {
		return "Square Donut";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.DECAY_SQUAREDONUT;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "decay_squaredonut";
	}

	@Override
	Location getSpawnLocation() {
		return new Location(this.getWorld(), -10.5, 65, 0);
	}

	@Override
	Location[] getBlocks() {
		final int r = 14;
		final int r2 = 5;
		final int y = 64;
		final List<Location> blocks = new ArrayList<>(r*r-r2*r2);
		for (int x = -r; x <= r; x++) {
			for (int z = -r; z <= r; z++) {
				if (x > r2 || x < -r2 || z > r2 || z < -r2) {
					blocks.add(new Location(this.getWorld(), x, y, z));
				}
			}
		}
		return blocks.toArray(new Location[] {});
	}

	@Override
	boolean isDead(final MPlayer player) {
		return player.getLocation().getY() < 50;
	}

}
