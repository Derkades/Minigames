package derkades.minigames.games.spleef;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import xyz.derkades.derkutils.bukkit.BlockUtils;

public class LittleStadium extends SpleefMap {

	@Override
	public Location getStartLocation() {
		return new Location(this.getWorld(), 0.0, 65, 0.5);
	}

	@Override
	public void fill() {
		BlockUtils.fillArea(this.getWorld(), 9, 64, -5, -10, 64, 5, Material.SNOW_BLOCK);
	}

	@Override
	public String getName() {
		return "Little Stadium";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.SPLEEF_LITTLESTADIUM;
	}

	@Override
	public boolean enableFlyingBlocks() {
		return false;
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
	public String getIdentifier() {
		return "spleef_littlestadium";
	}

}