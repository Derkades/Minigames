package xyz.derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import xyz.derkades.minigames.games.GameMap;

public abstract class CreeperAttackMap implements GameMap {
	
	public static final CreeperAttackMap[] MAPS = {
			new Mineshaft(),
			new Hedges(),
	};
	
	public abstract Location getCreeperLocation();
	
	public abstract Location getSpawnLocation();
	
	public abstract Location getSpectatorLocation();

}
