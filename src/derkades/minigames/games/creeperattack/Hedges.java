package derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class Hedges extends CreeperAttackMap {

	@Override
	public @NotNull String getName() {
		return "Hedges";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.CREEPERATTACK_HEDGES;
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
		return "creeperattack_hedges";
	}

	@Override
	Location getSpawnLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

	@Override
	Location getSpawnBoundsMin() {
		return new Location(this.getWorld(), -11, 64, -11);
	}

	@Override
	Location getSpawnBoundsMax() {
		return new Location(this.getWorld(), 11, 68, 11);
	}

}
