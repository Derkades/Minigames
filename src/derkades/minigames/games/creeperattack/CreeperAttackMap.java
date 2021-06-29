package derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import derkades.minigames.games.maps.GameMap;

public abstract class CreeperAttackMap extends GameMap {

	public static final CreeperAttackMap[] MAPS = {
			new DeckedOutForest(),
			new Mineshaft(),
			new Hedges(),
	};

	public abstract Location getCreeperLocation();

	public abstract Location getSpawnLocation();

}
