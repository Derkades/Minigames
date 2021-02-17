package derkades.minigames.games.takecover;

import org.bukkit.Location;

import derkades.minigames.games.maps.GameMap;

public abstract class TakeCoverMap extends GameMap {

	public static final TakeCoverMap[] MAPS = {
			new Prototype(),
	};
	
	public abstract Location getSpawnLocation();
	
	public abstract Location getCoverMin();
	
	public abstract Location getCoverMax();
	
}
