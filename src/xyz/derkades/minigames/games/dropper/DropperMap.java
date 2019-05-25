package xyz.derkades.minigames.games.dropper;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class DropperMap implements GameMap {
	
	public static DropperMap[] DROPPER_MAPS = new DropperMap[] {
			new BlackWhite(),
			new Rainbow(),
			new Redstone(),
			new Trees(),
	};
	
	//public abstract String getName();
	
	public abstract Location getLobbyLocation();
	
	public abstract void openDoor();
	
	public abstract void closeDoor();

}
