package xyz.derkades.minigames.games.snowfight;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Maze extends SnowFightMap {

	@Override
	public Location getSpawnLocation() {
		return new Location(Var.WORLD, 213.5, 46, 293.5);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 232.5, 46, 299.0, 180, 0);
	}

	@Override
	public String getName() {
		return "Maze";
	}

	@Override
	public MapSize getSize() {
		return MapSize.SMALL;
	}

	@Override
	public GameWorld getGameWorld() {
		return null;
	}

	@Override
	public String getCredits() {
		return null;
	}

}
