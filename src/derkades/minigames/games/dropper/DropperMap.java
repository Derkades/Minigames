package derkades.minigames.games.dropper;

import org.bukkit.Location;

import derkades.minigames.games.maps.GameMap;

abstract class DropperMap extends GameMap {

	static DropperMap[] DROPPER_MAPS = new DropperMap[] {
			new BlackWhite(),
			new Rainbow(),
			new Redstone(),
			new Trees(),
	};

	abstract Location getLobbyLocation();

	abstract void openDoor();

	abstract void closeDoor();

}
