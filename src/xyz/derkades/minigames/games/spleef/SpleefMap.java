package xyz.derkades.minigames.games.spleef;

import org.bukkit.Location;

import xyz.derkades.minigames.games.GameMap;

public abstract class SpleefMap implements GameMap {
	
	public static SpleefMap[] MAPS = {
		new BigStadium(),
		new LittleStadium(),
		new Original(),
	};
	
	public abstract Location getStartLocation();
	
	public abstract Location getSpectatorLocation();
	
	public abstract void fill();

}
