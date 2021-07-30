package derkades.minigames.games.hungergames;

import org.bukkit.Location;

import derkades.minigames.games.maps.GameMap;

public abstract class HungerGamesMap extends GameMap {

	public static final HungerGamesMap[] MAPS = {
			//new Prototype(),
			new Treehouse(),
			new Windmill(),
	};

	public abstract Location getCenterLocation();

	public abstract Location[] getStartLocations();

	public abstract Location[] getLootLevelOneLocations();

	public abstract Location[] getLootLevelTwoLocations();

	public abstract double getMaxBorderSize();

	public abstract double getMinBorderSize();

}
