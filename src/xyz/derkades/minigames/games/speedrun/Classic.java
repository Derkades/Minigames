package xyz.derkades.minigames.games.speedrun;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;

public class Classic extends SpeedrunMap {

	@Override
	public Location getStartLocation() {
		return new Location(Var.WORLD, 140.0, 97, 306, -180, 0);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 128.0, 98, 274.5, -180, 0);
	}

	@Override
	public String getName() {
		return "Classic";
	}

}
