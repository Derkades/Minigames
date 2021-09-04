package derkades.minigames.games.icyblowback;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class IcyBlowbackMap extends GameMap {

	static final IcyBlowbackMap[] MAPS = {
			new IcyBlowbackMapImpl(),
	};

	abstract Location[] getSpawnLocations();

	abstract int getBottomFloorLevel();


}
