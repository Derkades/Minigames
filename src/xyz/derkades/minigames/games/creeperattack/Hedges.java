package xyz.derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Hedges extends CreeperAttackMap {

	@Override
	public Location getCreeperLocation() {
		return new Location(Var.WORLD, 120.5, 105, 317.5);
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(Var.WORLD, 120.5, 105, 317.5);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 121.5, 109, 317.5);
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
		return null;
	}

}
