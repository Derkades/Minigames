package derkades.minigames.games.tron;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class TronMap extends GameMap {

	/**
	 * Used for determining if a player is outside the arena
	 */
	abstract Location getOuterCornerOne();

	/**
	 * Used for determining if a player is outside the arena
	 */
	abstract Location getOuterCornerTwo();

	/**
	 * Used for filling black concrete
	 */
	abstract Location getInnerCornerOne();

	/**
	 * Used for filling black concrete
	 */
	abstract Location getInnerCornerTwo();

	abstract TronSpawnLocation[] getSpawnLocations();

	abstract Location getSpectatorLocation();

}
