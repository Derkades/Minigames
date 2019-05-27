package xyz.derkades.minigames.games.sniper;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;

public class Snow extends SniperMap {

	@Override
	public String getName() {
		return "Snow";
	}

	@Override
	public MapSize getSize() {
		return MapSize.NORMAL;
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(Var.WORLD, 218.5, 75, 291.5, 90, 0);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 218.5, 75, 291.5, 90, 0);
	}

}
