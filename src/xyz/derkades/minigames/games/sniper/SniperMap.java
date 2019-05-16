package xyz.derkades.minigames.games.sniper;

import org.bukkit.Location;

import xyz.derkades.minigames.games.GameMap;

public abstract class SniperMap implements GameMap {
	
	public static final SniperMap[] MAPS = {
			new HouseWithFarm(),
	};
	
	public abstract Location getSpawnLocation();
	
	public abstract Location getSpectatorLocation();

}
