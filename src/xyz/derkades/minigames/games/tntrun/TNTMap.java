package xyz.derkades.minigames.games.tntrun;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class TNTMap implements GameMap {

	public static final TNTMap[] MAPS = {
			new Aqua(),
			new Future(),
			new Jungle(),
			new WaterLava(),
	};

	public abstract void restore();

	public abstract Material floorMaterial();

	public abstract Location spawnLocation();

}
