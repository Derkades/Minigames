package xyz.derkades.minigames.games.platform;

import org.bukkit.Location;

public abstract class PlatformMap {
	
	public static final PlatformMap[] MAPS = {
			new Desert(),
			new Ice(),
	};
	
	public abstract String getName();
	
	public abstract Location spawnLocation();
	
	public abstract Location spectatorLocation();

}
