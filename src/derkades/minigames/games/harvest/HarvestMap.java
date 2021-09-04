package derkades.minigames.games.harvest;

import java.util.List;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class HarvestMap extends GameMap {

	static final HarvestMap[] MAPS = {
			new Prototype(),
	};

	abstract List<Location> getCropLocations();

	Location getSpawnLocation() {
		return this.getWorld().getSpawnLocation();
	}

}
