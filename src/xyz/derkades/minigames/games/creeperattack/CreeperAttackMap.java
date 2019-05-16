package xyz.derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import xyz.derkades.minigames.games.GameMap;

public abstract class CreeperAttackMap implements GameMap {
	
	public static final CreeperAttackMap[] MAPS = {
			new Mineshaft(),
			new Original(),
	};
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}
	
	public abstract Location getCreeperLocation();
	
	public abstract Location getSpawnLocation();
	
	public abstract Location getSpectatorLocation();

}
