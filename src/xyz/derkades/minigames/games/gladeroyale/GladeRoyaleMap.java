package xyz.derkades.minigames.games.gladeroyale;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class GladeRoyaleMap extends GameMap {

	public static final GladeRoyaleMap[] MAPS = {
		new Santiago(),
	};

	public abstract int getWorldborderSize();

	public abstract Location getMapCenter();

	public abstract int getMinY();
	
	public abstract int getMaxY();
	
	public int getSpawnY() {
		return 200;
	}
	
}
