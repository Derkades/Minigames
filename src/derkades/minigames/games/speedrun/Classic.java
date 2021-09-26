package derkades.minigames.games.speedrun;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class Classic extends SpeedrunMap {

	@Override
	public @NotNull String getName() {
		return "Classic";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
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
	public @NotNull String getIdentifier() {
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
