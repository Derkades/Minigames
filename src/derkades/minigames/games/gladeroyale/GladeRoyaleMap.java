package derkades.minigames.games.gladeroyale;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class GladeRoyaleMap extends GameMap {

	abstract int getWorldborderSize();

	abstract Location getMapCenter();

	abstract int getMinY();

	abstract int getMaxY();

	int getSpawnY() {
		return 200;
	}

}
