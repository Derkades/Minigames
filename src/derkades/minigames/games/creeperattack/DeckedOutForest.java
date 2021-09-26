package derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class DeckedOutForest extends CreeperAttackMap {

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	Location getSpawnLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

	@Override
	public @NotNull String getName() {
		return "Decked Out (Forest)";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.CREEPERATTACK_DECKEDOUTFOREST;
	}

	@Override
	public String getCredits() {
		return "Hermitcraft";
	}

	@Override
	public @NotNull String getIdentifier() {
		return "creeperattack_deckedoutforest";
	}

	@Override
	public void onStart() {
		this.getWorld().setTime(18000);
	}

	@Override
	Location getSpawnBoundsMin() {
		return new Location(this.getWorld(), -21, 60, -22);
	}

	@Override
	Location getSpawnBoundsMax() {
		return new Location(this.getWorld(), 24, 77, 18);
	}

}
