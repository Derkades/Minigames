package xyz.derkades.minigames.games.dropper;

import org.bukkit.Location;

public abstract class DropperMap {
	
	public static DropperMap[] DROPPER_MAPS = new DropperMap[] {
			new BlackWhite(),
			new Rainbow(),
			new Trees(),
	};
	
	public abstract String getName();
	
	public abstract Location getLobbyLocation();
	
	public abstract void openDoor();
	
	public abstract void closeDoor();

}
