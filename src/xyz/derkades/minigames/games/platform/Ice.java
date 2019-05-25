package xyz.derkades.minigames.games.platform;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;

public class Ice extends PlatformMap {

	@Override
	public Location spawnLocation() {
		return new Location(Var.WORLD, 229.5, 88, 186.5, 0, 0);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 229.5, 93, 178.0, 0, 0);
	}

	@Override
	public String getName() {
		return "Ice";
	}
	
	@Override
	public MapSize getSize() {
		return MapSize.NORMAL;
	}

}
