package derkades.minigames.games.platform;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class PlatformMap extends GameMap {

	static final PlatformMap[] MAPS = {
			new Desert(),
			new Ice(),
	};

	abstract Location getSpawnLocation();

}
