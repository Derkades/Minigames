package xyz.derkades.minigames.games.spleef;

import org.bukkit.Location;

public abstract class SpleefMap {
	
	public static SpleefMap[] MAPS = {
		new Original(),
		new Stadium(),
	};
	
	public abstract Location getStartLocation();
	
	public abstract Location getSpectatorLocation();
	
	public abstract void fill();

}