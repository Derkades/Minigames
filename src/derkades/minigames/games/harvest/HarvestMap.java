package derkades.minigames.games.harvest;

import derkades.minigames.games.GameMap;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

abstract class HarvestMap extends GameMap {

	@NotNull
	abstract List<Location> getCropLocations();

	@NotNull
	Location getSpawnLocation() {
		return this.getWorld().getSpawnLocation();
	}

}
