package derkades.minigames.games.harvest;

import java.util.List;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import derkades.minigames.games.GameMap;

abstract class HarvestMap extends GameMap {

	@NotNull
	static final HarvestMap@NotNull[] MAPS = {
			new Prototype(),
	};

	@NotNull
	abstract List<Location> getCropLocations();

	@NotNull
	Location getSpawnLocation() {
		return this.getWorld().getSpawnLocation();
	}

}
