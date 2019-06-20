package xyz.derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Hedges extends CreeperAttackMap {

	@Override
	public Location getCreeperLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

	@Override
	public String getName() {
		return "Hedges";
	}

	@Override
	public MapSize getSize() {
		return MapSize.NORMAL;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.CREEPERATTACK_HEDGES;
	}

}
