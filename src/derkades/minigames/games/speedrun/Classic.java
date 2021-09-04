package derkades.minigames.games.speedrun;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

class Classic extends SpeedrunMap {

	@Override
	public String getName() {
		return "Classic";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.SPEEDRUN_CLASSIC;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return Size.NORMAL;
	}

	@Override
	public String getIdentifier() {
		return "speedrun_classic";
	}

	@Override
	Location getStartLocation() {
		return new Location(this.getWorld(), 1.0, 65, 1.0, -180, 0);
	}

	@Override
	Location getSpectatorLocation() {
		return new Location(this.getWorld(), -10, 74, -57);
	}

}
