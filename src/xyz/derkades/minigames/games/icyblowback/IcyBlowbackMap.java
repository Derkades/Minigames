package xyz.derkades.minigames.games.icyblowback;

import org.bukkit.Location;

public abstract class IcyBlowbackMap {

	public static final IcyBlowbackMap[] MAPS = {
			new IcyBlowback(),
	};
	
	public abstract Location getSpectatorLocation();
	
	public abstract Location[] getSpawnLocations();
	
	
}
