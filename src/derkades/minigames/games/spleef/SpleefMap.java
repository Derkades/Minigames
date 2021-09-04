package derkades.minigames.games.spleef;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

public abstract class SpleefMap extends GameMap {

	public static SpleefMap[] MAPS = {
		new BigStadium(),
		new LittleStadium(),
		new Original(),
	};

	public abstract Location getStartLocation();

	public abstract void fill();

	public abstract boolean enableFlyingBlocks();

}
