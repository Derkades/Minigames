package xyz.derkades.minigames.games.platform;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Desert extends PlatformMap {

	@Override
	public Location getSpawnLocation() {
		return new Location(Var.WORLD, 209.5, 87, 362.5);
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
		return null;
	}

}
