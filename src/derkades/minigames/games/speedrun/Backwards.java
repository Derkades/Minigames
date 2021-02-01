package derkades.minigames.games.speedrun;

import org.bukkit.Location;

import derkades.minigames.Var;
import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

public class Backwards extends SpeedrunMap {

	@Override
	public Location getStartLocation() {
		return new Location(Var.WORLD, 14.5, 102.5, 189.5, 0, 0);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 10.5, 105.5, 197.5, -180, 0);
	}

	@Override
	public String getName() {
		return "Backwards";
	}

	@Override
	public GameWorld getGameWorld() {
		return null;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return null;
	}

}
