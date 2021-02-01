package derkades.minigames.games.icyblowback;

import org.bukkit.Location;

import derkades.minigames.games.maps.GameMap;

public abstract class IcyBlowbackMap extends GameMap {

	public static final IcyBlowbackMap[] MAPS = {
			new IcyBlowback(),
	};

	public abstract Location[] getSpawnLocations();

	public abstract int getBottomFloorLevel();


}
