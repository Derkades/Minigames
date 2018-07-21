package xyz.derkades.minigames.games.platform;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;

public class Ice extends PlatformMap {

	@Override
	public Location spawnLocation() {
		return new Location(Var.WORLD, 229.5, 88, 186.5, 0, 0);
	}

	@Override
	public Location spectatorLocation() {
		return new Location(Var.WORLD, 229.5, 93, 178.0, 0, 0);
	}

	@Override
	public String getName() {
		return "Ice";
	}

}
