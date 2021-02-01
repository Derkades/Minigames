package derkades.minigames.games.speedrun;

import org.bukkit.Location;

import derkades.minigames.Var;
import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

public class Construction extends SpeedrunMap {

	@Override
	public Location getStartLocation() {
		return new Location(Var.WORLD, -24.5, 102, 189.5);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, -27.5, 108.5, 236.5);
	}

	@Override
	public String getName() {
		return "Construction";
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
