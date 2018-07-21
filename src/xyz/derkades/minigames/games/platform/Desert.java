package xyz.derkades.minigames.games.platform;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;

public class Desert extends PlatformMap {

	@Override
	public Location spawnLocation() {
		return new Location(Var.WORLD, 209.5, 87, 362.5, 90, 90);
	}

	@Override
	public Location spectatorLocation() {
		return new Location(Var.WORLD, 95, 87, 362.5, 90, 90);
	}

	@Override
	public String getName() {
		return "Desert";
	}

}
