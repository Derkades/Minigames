package derkades.minigames.games.gladeroyale;

import org.bukkit.Location;

import derkades.minigames.games.maps.GameMap;

abstract class GladeRoyaleMap extends GameMap {

	static final GladeRoyaleMap[] MAPS = {
		new Santiago(),
	};

	abstract int getWorldborderSize();

	abstract Location getMapCenter();

	abstract int getMinY();

	abstract int getMaxY();

	int getSpawnY() {
		return 200;
	}

}
