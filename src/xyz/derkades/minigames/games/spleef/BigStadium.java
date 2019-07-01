package xyz.derkades.minigames.games.spleef;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.worlds.GameWorld;

public class BigStadium extends SpleefMap {

	@Override
	public Location getStartLocation() {
		return new Location(this.getWorld(), 0, 65, 0);
	}

	@Override
	public void fill() {
		BlockUtils.fillArea(this.getWorld(), 22, 64, -12, -22, 64, 13, Material.SNOW_BLOCK);
	}

	@Override
	public String getName() {
		return "Big Stadium";
	}

	@Override
	public MapSize getSize() {
		return MapSize.LARGE;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.SPLEEF_BIGSTADIUM;
	}

	@Override
	public boolean enableFlyingBlocks() {
		return false;
	}

	@Override
	public String getCredits() {
		return "MissChikoo";
	}

}
