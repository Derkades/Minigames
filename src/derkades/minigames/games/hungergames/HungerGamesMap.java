package derkades.minigames.games.hungergames;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class HungerGamesMap extends GameMap {

	abstract Location getCenterLocation();

	abstract Location[] getStartLocations();

	abstract Location[] getLootLevelOneLocations();

	abstract Location[] getLootLevelTwoLocations();

	abstract double getMaxBorderSize();

	abstract double getMinBorderSize();

}
