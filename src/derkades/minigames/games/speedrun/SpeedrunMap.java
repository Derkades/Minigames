package derkades.minigames.games.speedrun;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.games.GameMap;

public abstract class SpeedrunMap extends GameMap {

	public static final SpeedrunMap[] MAPS = {
//		new Backwards(),
		new Classic(),
//		new Construction(),
	};

	public Material getFloorBlock() {
		return Material.RED_TERRACOTTA;
	}

	public Material getEndBlock() {
		return Material.GRAY_TERRACOTTA;
	}

	public abstract Location getStartLocation();

	public abstract Location getSpectatorLocation();

}
