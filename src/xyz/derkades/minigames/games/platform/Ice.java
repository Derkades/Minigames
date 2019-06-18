package xyz.derkades.minigames.games.platform;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Ice extends PlatformMap {

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), -32.5, 71, 10.5, -180, 0);
	}

	@Override
	public String getName() {
		return "Ice";
	}

	@Override
	public MapSize getSize() {
		return MapSize.NORMAL;
	}

	@Override
	public GameWorld getGameWorld() {
		return null;
	}

}
