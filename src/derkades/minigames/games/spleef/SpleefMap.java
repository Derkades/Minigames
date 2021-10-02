package derkades.minigames.games.spleef;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class SpleefMap extends GameMap {

	abstract Location getStartLocation();

	abstract void fill();

	abstract boolean enableFlyingBlocks();

}
