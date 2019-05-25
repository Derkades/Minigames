package xyz.derkades.minigames.games.molepvp;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class MolePvPMap implements GameMap {
	
	public static final MolePvPMap[] MAPS = {
			new Prototype(),
	};
	
	public abstract void setupMap();
	
	public abstract Location getTeamRedSpawnLocation();
	
	public abstract Location getTeamBlueSpawnLocation();
	

}
