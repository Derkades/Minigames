package xyz.derkades.minigames.games.icyblowback;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class IcyBlowbackMap implements GameMap {

	public static final IcyBlowbackMap[] MAPS = {
			new IcyBlowback(),
	};
	
	public abstract Location getSpectatorLocation();
	
	public abstract Location[] getSpawnLocations();
	
	
}
