package derkades.minigames.games.speedrun;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.games.GameMap;

abstract class SpeedrunMap extends GameMap {

	public static final SpeedrunMap[] MAPS = {
//		new Backwards(),
		new Classic(),
//		new Construction(),
	};

	Material getFloorBlock() {
		return Material.RED_TERRACOTTA;
	}

	Material getEndBlock() {
		return Material.GRAY_TERRACOTTA;
	}

	abstract Location getStartLocation();

	abstract Location getSpectatorLocation();

}
