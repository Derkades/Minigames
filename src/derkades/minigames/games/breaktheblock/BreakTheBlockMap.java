package derkades.minigames.games.breaktheblock;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

public abstract class BreakTheBlockMap extends GameMap {

	static final BreakTheBlockMap[] MAPS = {
			new Prototype(),
//			new Jungle(),
	};

	abstract Location getStartLocation();

}
