package xyz.derkades.minigames.games.sniper;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;

public class HouseWithFarm extends SniperMap {

	@Override
	public Location getSpawnLocation() {
		return new Location(Var.WORLD, 183.5, 86, 213.5);
	}

	@Override
	public Location getSpectatorLocation() {
		return null;
	}

}
