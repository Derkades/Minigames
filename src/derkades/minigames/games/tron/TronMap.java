package derkades.minigames.games.tron;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

public abstract class TronMap extends GameMap {

	public static final TronMap[] MAPS = {
			new Prototype(),
	};

	/**
	 * Used for determining if a player is outside of the arena
	 */
	public abstract Location getOuterCornerOne();

	/**
	 * Used for determining if a player is outside of the arena
	 */
	public abstract Location getOuterCornerTwo();

	/**
	 * Used for filling black concrete
	 */
	public abstract Location getInnerCornerOne();

	/**
	 * Used for filling black concrete
	 */
	public abstract Location getInnerCornerTwo();

	public abstract TronSpawnLocation[] getSpawnLocations();

	public abstract Location getSpectatorLocation();

//	public abstract Direction getSpawnDirection(GameTeam team);

}
