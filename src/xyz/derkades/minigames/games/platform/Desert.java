package xyz.derkades.minigames.games.platform;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;

public class Desert extends PlatformMap {

	@Override
	public Location spawnLocation() {
		return new Location(Var.WORLD, 209.5, 87, 362.5);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 209.5, 95, 362.5);
	}

	@Override
	public String getName() {
		return "Desert";
	}
	
	@Override
	public MapSize getSize() {
		return MapSize.NORMAL;
	}

}
