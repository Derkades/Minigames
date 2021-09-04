package derkades.minigames.games.hungergames;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class HungerGamesMap extends GameMap {

	static final HungerGamesMap[] MAPS = {
			//new Prototype(),
			new Treehouse(),
			new Windmill(),
	};

	abstract Location getCenterLocation();

	abstract Location[] getStartLocations();

	abstract Location[] getLootLevelOneLocations();

	abstract Location[] getLootLevelTwoLocations();

	abstract double getMaxBorderSize();

	abstract double getMinBorderSize();

}
