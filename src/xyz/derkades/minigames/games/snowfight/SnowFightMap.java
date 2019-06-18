package xyz.derkades.minigames.games.snowfight;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class SnowFightMap extends GameMap {

	public static final SnowFightMap[] MAPS = {
			new Maze(),
			new Original(),
	};

	public abstract Location getSpawnLocation();

	public abstract Location getSpectatorLocation();

}
