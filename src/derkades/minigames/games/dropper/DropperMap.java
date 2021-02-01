package derkades.minigames.games.dropper;

import org.bukkit.Location;

import derkades.minigames.games.maps.GameMap;

public abstract class DropperMap extends GameMap {

	public static DropperMap[] DROPPER_MAPS = new DropperMap[] {
			new BlackWhite(),
			new Rainbow(),
			new Redstone(),
			new Trees(),
	};

	public abstract Location getLobbyLocation();

	public abstract void openDoor();

	public abstract void closeDoor();

}
