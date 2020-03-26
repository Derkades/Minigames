package xyz.derkades.minigames.games.pointcontrol;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.utils.MPlayer;

public abstract class ControlPointsMap extends GameMap {
	
	public static final ControlPointsMap[] MAPS = {
			new Prototype(),
	};
	
	public abstract Location getBlueSpawnLocation();
	
	public abstract Location getRedSpawnLocation();
	
	public abstract Location[] getControlPointLocations();
	
	public abstract boolean isOnControlPoint(final Location location, final MPlayer player);
	
	public abstract void setControlPointStatus(Location location, ControlStatus status);

}