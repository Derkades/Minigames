package xyz.derkades.minigames.games.snowfight;

import org.bukkit.Location;

public abstract class SnowFightMap {
	
	public static final SnowFightMap[] MAPS = {
			new Maze(),
			new Original(),
	};
	
	public abstract Location getSpawnLocation();
	
	public abstract Location getSpectatorLocation();

}
