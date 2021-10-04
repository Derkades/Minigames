package derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class Mineshaft extends CreeperAttackMap {

	@Override
	Location getSpawnLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5, -180f, 0f);
	}

	@Override
	public @NotNull String getName() {
		return "Mineshaft";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.CREEPERATTACK_MINESHAFT;
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
		return "creeperattack_mineshaft";
	}

	@Override
	Location getSpawnBoundsMin() {
		return new Location(this.getWorld(), -4, 64, -10);
	}

	@Override
	Location getSpawnBoundsMax() {
		return new Location(this.getWorld(), 13, 70, 3);
	}

	@Override
	public boolean isDisabled() {
		return true;
	}

}