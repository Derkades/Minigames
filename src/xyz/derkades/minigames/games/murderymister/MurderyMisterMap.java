package xyz.derkades.minigames.games.murderymister;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class MurderyMisterMap extends GameMap {

	public static final MurderyMisterMap[] MAPS = {
			new HauntedHouse(),
	};

	public abstract Location[] getSpawnLocations();

	public abstract Location[] getFlickeringRedstomeLamps();

	public abstract Location[] getCandles();

}
