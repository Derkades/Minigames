package xyz.derkades.minigames.games.digdug;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class DigDugMap extends GameMap {

	public static final DigDugMap[] MAPS = {
			new Prototype(),
	};

	public abstract Location getBlocksMinLocation();

	public abstract Location getBlocksMaxLocation();

	public abstract Location getSpawnLocation();

}
