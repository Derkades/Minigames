package xyz.derkades.minigames.games.speedrun;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;

public class Construction extends SpeedrunMap {

	@Override
	public Location getStartLocation() {
		return new Location(Var.WORLD, -24.5, 102, 189.5);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, -27.5, 108.5, 236.5);
	}

}
