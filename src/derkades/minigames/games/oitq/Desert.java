package derkades.minigames.games.oitq;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class Desert extends OITQMap {

	@Override
	public @NotNull String getName() {
		return "Desert";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.OITQ_DESERT;
	}

	@Override
	public String getCredits() {
		return "Corrupt_World";
	}

	@Override
	public Size getSize() {
		return Size.NORMAL;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "oneinthequiver_desert";
	}

	@Override
	Location getSpawnLocation() {
		return new Location(this.getWorld(), -27, 72, -4);
	}

}
