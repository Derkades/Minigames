package xyz.derkades.minigames.games.snowfight;

import org.bukkit.Location;

import xyz.derkades.minigames.games.GameMap;

public abstract class SnowFightMap implements GameMap {
	
	public static final SnowFightMap[] MAPS = {
			new Maze(),
			new Original(),
	};
	
	public abstract Location getSpawnLocation();
	
	public abstract Location getSpectatorLocation();

}