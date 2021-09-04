package derkades.minigames.games.platform;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

public abstract class PlatformMap extends GameMap {

	public static final PlatformMap[] MAPS = {
			new Desert(),
			new Ice(),
	};

	public abstract Location getSpawnLocation();

}
