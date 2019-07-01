package xyz.derkades.minigames.games.mysterymurder;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class MurderyMisterMap extends GameMap {

	public static final MurderyMisterMap[] MAPS = {

	};

	public abstract Location[] getSpawnLocations();

	public abstract Location[] getFlickeringRedstomeLamps();

	public abstract Location[] getCandles();

}
