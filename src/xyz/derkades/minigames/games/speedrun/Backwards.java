package xyz.derkades.minigames.games.speedrun;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;

public class Backwards extends SpeedrunMap {

	@Override
	public Location getStartLocation() {
		return new Location(Var.WORLD, 14.5, 102.5, 189.5, 0, 0);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 10.5, 105.5, 197.5, -180, 0);
	}

}