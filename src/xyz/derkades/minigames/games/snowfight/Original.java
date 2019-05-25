package xyz.derkades.minigames.games.snowfight;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;

public class Original extends SnowFightMap {

	@Override
	public Location getSpawnLocation() {
		return new Location(Var.WORLD, 218.5, 75, 291.5, 90, 0);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 224.5, 79.1, 291.5, 90, 0);
	}

	@Override
	public String getName() {
		return "Snow";
	}

	@Override
	public MapSize getSize() {
		return MapSize.NORMAL;
	}


}
