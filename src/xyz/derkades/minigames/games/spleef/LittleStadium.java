package xyz.derkades.minigames.games.spleef;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.worlds.GameWorld;

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
	public MapSize getSize() {
		return MapSize.NORMAL;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.SPLEEF_LITTLESTADIUM;
	}

	@Override
	public boolean enableFlyingBlocks() {
		return false;
	}

}