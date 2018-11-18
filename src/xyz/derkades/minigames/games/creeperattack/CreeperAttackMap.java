package xyz.derkades.minigames.games.creeperattack;

import org.bukkit.Location;

public abstract class CreeperAttackMap {
	
	public static final CreeperAttackMap[] MAPS = {
			new Mineshaft(),
			new Original(),
	};
	
	public abstract Location getCreeperLocation();
	
	public abstract Location getSpawnLocation();
	
	public abstract Location getSpectatorLocation();

}
