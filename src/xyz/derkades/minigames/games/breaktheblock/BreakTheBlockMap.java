package xyz.derkades.minigames.games.breaktheblock;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class BreakTheBlockMap implements GameMap {

	public static final BreakTheBlockMap[] MAPS = {
			new Prototype(),
	};

	public abstract void onPreStart();

	public abstract void onStart();

	public abstract void timer();

	public abstract Location getStartLocation();

}
