package derkades.minigames.games.tron;

import java.util.Map;

import org.bukkit.Location;

import derkades.minigames.games.Tron.Direction;
import derkades.minigames.games.Tron.TronTeam;
import derkades.minigames.games.maps.GameMap;

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

	public abstract Map<TronTeam, Location> getSpawnLocations();
	
	public abstract Direction getSpawnDirection(TronTeam team);
	
}
