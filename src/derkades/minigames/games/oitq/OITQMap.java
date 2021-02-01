package derkades.minigames.games.oitq;

import org.bukkit.Location;

import derkades.minigames.games.maps.GameMap;

public abstract class OITQMap extends GameMap {

	public static final OITQMap[] MAPS = {
			new Barn(),
			new Castle(),
			new Desert(),
			new HouseWithFarm(),
			new Snow(),
	};

	public abstract Location getSpawnLocation();

}
