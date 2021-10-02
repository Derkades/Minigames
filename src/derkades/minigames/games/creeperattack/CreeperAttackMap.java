package derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

public abstract class CreeperAttackMap extends GameMap {
	
	abstract Location getSpawnLocation();

	abstract Location getSpawnBoundsMin();

	abstract Location getSpawnBoundsMax();

}
