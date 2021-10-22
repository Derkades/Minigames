package derkades.minigames.games.spleef;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

class Original extends SpleefMap {

	@Override
	public @NotNull String getName() {
		return "Original";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.SPLEEF_ORIGINAL;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return Size.SMALL;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "spleef_original";
	}

	@Override
	Location getStartLocation() {
		return new Location(this.getWorld(), 0, 65, 0);
	}

	@Override
	void fill() {
//		BlockUtils.fillArea(this.getWorld(), -7, 64, -7, 7, 64, 7, Material.SNOW_BLOCK);
	}

	@Override
	boolean enableFlyingBlocks() {
		return true;
	}

}
