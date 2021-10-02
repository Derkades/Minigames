package derkades.minigames.games.murderymister;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class MurderyMisterMap extends GameMap {

	abstract Location[] getSpawnLocations();

	abstract Location[] getFlickeringRedstoneLamps();

	abstract Location[] getCandles();

}
