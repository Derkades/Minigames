package xyz.derkades.minigames.games.harvest;

import java.util.List;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class HarvestMap extends GameMap {

	public static final HarvestMap[] MAPS = {
			new Prototype(),
	};
	
	public abstract List<Location> getCropLocations();

	public Location getSpawnLocation() {
		return this.getWorld().getSpawnLocation();
	}

}
