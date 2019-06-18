package xyz.derkades.minigames.games.sniper;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class SniperMap extends GameMap {

	public static final SniperMap[] MAPS = {
			new Desert(),
			new HouseWithFarm(),
			new Snow(),
	};

	public abstract Location getSpawnLocation();

}
