package xyz.derkades.minigames.games.bowspleef;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class BowSpleefMap extends GameMap {

	public static final BowSpleefMap[] MAPS = {
			new BowSpleef(),
	};

	public abstract Location getSpawnLocation();

}
