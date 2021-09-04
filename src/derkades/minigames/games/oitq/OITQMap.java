package derkades.minigames.games.oitq;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class OITQMap extends GameMap {

	static final OITQMap[] MAPS = {
			new Barn(),
			new Castle(),
			new Desert(),
			new HouseWithFarm(),
			new Snow(),
	};

	abstract Location getSpawnLocation();

}
