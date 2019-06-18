package xyz.derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Mineshaft extends CreeperAttackMap {

	@Override
	public Location getCreeperLocation() {
		return new Location(Var.WORLD, 288, 39, 228);

	}

	@Override
	public Location getSpawnLocation() {
		return new Location(Var.WORLD, 288, 39, 228);
	}

	@Override
	public Location getSpectatorLocation() {
		return null;
	}

	@Override
	public String getName() {
		return "Mineshaft";
	}

	@Override
	public MapSize getSize() {
		return MapSize.SMALL;
	}

	@Override
	public GameWorld getGameWorld() {
		return null;
	}

}