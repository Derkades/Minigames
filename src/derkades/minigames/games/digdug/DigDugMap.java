package derkades.minigames.games.digdug;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class DigDugMap extends GameMap {

	abstract Location getBlocksMinLocation();

	abstract Location getBlocksMaxLocation();

	abstract Location getSpawnLocation();

}
