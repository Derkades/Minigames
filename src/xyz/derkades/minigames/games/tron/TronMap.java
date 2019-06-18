package xyz.derkades.minigames.games.tron;

import java.util.Map;

import org.bukkit.Location;

import xyz.derkades.minigames.games.Tron.TronTeam;
import xyz.derkades.minigames.games.maps.GameMap;

public abstract class TronMap extends GameMap {

	public static final TronMap[] MAPS = {
			new Prototype(),
	};

	public abstract Location getOuterCornerOne();

	public abstract Location getOuterCornerTwo();

	public abstract Location getInnerCornerOne();

	public abstract Location getInnerCornerTwo();

	public abstract Location getSpectatorSpawnLocation();

	public abstract Map<TronTeam, Location> getSpawnLocations();
}
