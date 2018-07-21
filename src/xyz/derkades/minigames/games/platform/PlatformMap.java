package xyz.derkades.minigames.games.platform;

import org.bukkit.Location;

public abstract class PlatformMap {
	
	public static final PlatformMap[] MAPS = {
			
	};
	
	public abstract String getName();
	
	public abstract Location spawnLocation();
	
	public abstract Location spectatorLocation();

}
