package xyz.derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class CreeperAttackMap extends GameMap {

	public static final CreeperAttackMap[] MAPS = {
			new Mineshaft(),
			new Hedges(),
	};

	public abstract Location getCreeperLocation();

	public abstract Location getSpawnLocation();

}
