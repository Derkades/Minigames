package derkades.minigames.games.takecover;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class TakeCoverMap extends GameMap {

	abstract Location getSpawnLocation();

	abstract Location getCoverMin();

	abstract Location getCoverMax();

}
