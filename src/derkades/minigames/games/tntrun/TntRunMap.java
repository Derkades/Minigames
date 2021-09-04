package derkades.minigames.games.tntrun;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.games.GameMap;

abstract class TntRunMap extends GameMap {

	static final TntRunMap[] MAPS = {
			new Aqua(),
			new Future(),
			new Jungle(),
			new WaterLava(),
	};

	abstract void restore();

	abstract Material floorMaterial();

	abstract Location spawnLocation();

}
