package xyz.derkades.minigames.games.hungergames;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class HungerGamesMap extends GameMap {

	public static final HungerGamesMap[] MAPS = {
			new Prototype(),
	};

	public abstract Location[] getStartLocations();

	public abstract Location[] getLootLevelOneLocations();

	public abstract Location[] getLootLevelTwoLocations();

}
