package xyz.derkades.minigames.games.speedrun;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

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

	@Override
	public String getName() {
		return "Trees";
	}

	@Override
	public MapSize getSize() {
		return null;
	}

	@Override
	public GameWorld getGameWorld() {
		return null;
	}

	@Override
	public String getCredits() {
		return null;
	}

}
