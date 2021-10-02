package derkades.minigames.games.takecover;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class Prototype extends TakeCoverMap {

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	Location getSpawnLocation() {
		return this.getWorld().getSpawnLocation();
	}

	@Override
	Location getCoverMin() {
		return new Location(this.getWorld(), -8, 67, -9);
	}

	@Override
	Location getCoverMax() {
		return new Location(this.getWorld(), 8, 67, 8);
	}

	@Override
	public @NotNull String getName() {
		return "Prototype";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.TAKECOVER_PROTOTYPE;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "takecover_prototype";
	}

}
