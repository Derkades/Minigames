package xyz.derkades.minigames.games.platform;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class PlatformMap implements GameMap {
	
	public static final PlatformMap[] MAPS = {
			new Desert(),
			new Ice(),
	};
	
	public abstract Location spawnLocation();
	
	public abstract Location getSpectatorLocation();

}
