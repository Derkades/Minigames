package xyz.derkades.minigames.games.breaktheblock;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class BreakTheBlockMap extends GameMap {

	public static final BreakTheBlockMap[] MAPS = {
			new Prototype(),
			new Jungle(),
	};

	public abstract Location getStartLocation();

}
