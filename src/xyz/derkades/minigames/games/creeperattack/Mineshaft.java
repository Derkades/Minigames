package xyz.derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;

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

}