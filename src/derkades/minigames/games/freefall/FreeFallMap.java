package derkades.minigames.games.freefall;

import org.bukkit.Location;

import derkades.minigames.games.maps.GameMap;

abstract class FreeFallMap extends GameMap {

	static final FreeFallMap[] MAPS = {
			new Prototype(),
	};

	abstract Location getSpawnLocation();

	abstract Layer[] getLayers();

}
