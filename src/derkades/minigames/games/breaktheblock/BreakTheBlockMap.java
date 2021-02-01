package derkades.minigames.games.breaktheblock;

import org.bukkit.Location;

import derkades.minigames.games.maps.GameMap;

public abstract class BreakTheBlockMap extends GameMap {

	public static final BreakTheBlockMap[] MAPS = {
			new Prototype(),
//			new Jungle(),
	};

	public abstract Location getStartLocation();

}
