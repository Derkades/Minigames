package derkades.minigames.games.dropper;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class DropperMap extends GameMap {

	abstract Location getLobbyLocation();

	abstract void openDoor();

	abstract void closeDoor();

}
