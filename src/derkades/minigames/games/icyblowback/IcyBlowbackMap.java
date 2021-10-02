package derkades.minigames.games.icyblowback;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class IcyBlowbackMap extends GameMap {

	abstract Location[] getSpawnLocations();

	abstract int getBottomFloorLevel();


}
