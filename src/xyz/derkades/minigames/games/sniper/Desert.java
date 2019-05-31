package xyz.derkades.minigames.games.sniper;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;

public class Desert extends SniperMap {

	@Override
	public String getName() {
		return "Desert";
	}

	@Override
	public MapSize getSize() {
		return MapSize.LARGE;
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(Var.WORLD, 466, 73, 187);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 466, 73, 187);
	}

}
