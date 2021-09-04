package derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

public abstract class CreeperAttackMap extends GameMap {

	static final CreeperAttackMap[] MAPS = {
			new DeckedOutForest(),
			new Mineshaft(),
			new Hedges(),
	};

	abstract Location getCreeperLocation();

	abstract Location getSpawnLocation();

}
