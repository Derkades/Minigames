package derkades.minigames.games.freefall;

import org.bukkit.Location;

import derkades.minigames.games.maps.GameMap;

public abstract class FreeFallMap extends GameMap {

	public static final FreeFallMap[] MAPS = {
			new Prototype(),
	};

	public abstract Location getSpawnLocation();

	public abstract Layer[] getLayers();

}
