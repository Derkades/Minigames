package xyz.derkades.minigames.games.snowfight;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;

public class Maze extends SnowFightMap {

	@Override
	public Location getSpawnLocation() {
		return new Location(Var.WORLD, 213.5, 46, 293.5);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 232.5, 46, 299.0, 180, 0);
	}

}
