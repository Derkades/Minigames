package xyz.derkades.minigames.games.platform;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Desert extends PlatformMap {

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), 0, 65, 0);
	}

	@Override
	public String getName() {
		return "Desert";
	}

	@Override
	public MapSize getSize() {
		return MapSize.NORMAL;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.PLATFORM_DESERT;
	}

	@Override
	public String getCredits() {
		return null;
	}

}
