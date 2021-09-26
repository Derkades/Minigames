package derkades.minigames.games.spleef;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;
import xyz.derkades.derkutils.bukkit.BlockUtils;

class LittleStadium extends SpleefMap {

	@Override
	public @NotNull String getName() {
		return "Little Stadium";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.SPLEEF_LITTLESTADIUM;
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
		return "spleef_littlestadium";
	}

	@Override
	Location getStartLocation() {
		return new Location(this.getWorld(), 0.0, 65, 0.5);
	}

	@Override
	void fill() {
		BlockUtils.fillArea(this.getWorld(), 9, 64, -5, -10, 64, 5, Material.SNOW_BLOCK);
	}

	@Override
	public boolean enableFlyingBlocks() {
		return false;
	}

}