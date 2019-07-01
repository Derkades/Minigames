package xyz.derkades.minigames.games.mysterymurder;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class MysteryMurderMap extends GameMap {

	public abstract Location[] getSpawnLocations();

	public abstract Location[] getFlickeringRedstomeLamps();

	public abstract Location[] getCandles();

}
