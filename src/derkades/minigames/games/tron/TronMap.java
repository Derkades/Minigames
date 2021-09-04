package derkades.minigames.games.tron;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class TronMap extends GameMap {

	static final TronMap[] MAPS = {
			new Prototype(),
	};

	/**
	 * Used for determining if a player is outside of the arena
	 */
	abstract Location getOuterCornerOne();

	/**
	 * Used for determining if a player is outside of the arena
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
