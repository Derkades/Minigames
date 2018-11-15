package xyz.derkades.minigames.games.sniper;

import org.bukkit.Location;

public abstract class SniperMap {
	
	public static final SniperMap[] MAPS = {
			new HouseWithFarm(),
	};
	
	public abstract Location getSpawnLocation();
	
	public abstract Location getSpectatorLocation();

}
