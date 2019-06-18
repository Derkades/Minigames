package xyz.derkades.minigames.games.harvest;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class HarvestMap extends GameMap {

	public static final HarvestMap[] MAPS = {
			new Prototype(),
	};

	public abstract void restoreMap();

	public abstract Location getSpawnLocation();

}
