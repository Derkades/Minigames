package xyz.derkades.minigames.games.speedrun;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;

/**
 * @deprecated Map is not finished
 */
@Deprecated
public class Trees extends SpeedrunMap {

	@Override
	public Location getStartLocation() {
		return new Location(Var.WORLD, 63.5, 102.5, 188.5, 0, 0);
	}

	@Override
	public Location getSpectatorLocation() {
		return null;
	}

}
