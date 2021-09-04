package derkades.minigames.games.controlpoints;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;
import derkades.minigames.utils.MPlayer;

abstract class ControlPointsMap extends GameMap {

	static final ControlPointsMap[] MAPS = {
			new Prototype(),
	};

	abstract Location getBlueSpawnLocation();

	abstract Location getRedSpawnLocation();

	abstract Location[] getControlPointLocations();

	abstract boolean isOnControlPoint(final Location location, final MPlayer player);

	abstract void setControlPointStatus(Location location, ControlStatus status);

}
