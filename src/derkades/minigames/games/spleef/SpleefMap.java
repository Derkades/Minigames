package derkades.minigames.games.spleef;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class SpleefMap extends GameMap {

	static SpleefMap[] MAPS = {
		new BigStadium(),
		new LittleStadium(),
		new Original(),
	};

	abstract Location getStartLocation();

	abstract void fill();

	abstract boolean enableFlyingBlocks();

}
