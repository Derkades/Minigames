package derkades.minigames.games.oitq;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class Snow extends OITQMap {

	@Override
	public @NotNull String getName() {
		return "Snow";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.OITQ_SNOW;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return Size.SMALL;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "oneinthequiver_snow";
	}

	@Override
	Location getSpawnLocation() {
		return new Location(this.getWorld(), -22, 66, -1);
	}

}
