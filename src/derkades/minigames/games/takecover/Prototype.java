package derkades.minigames.games.takecover;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

public class Prototype extends TakeCoverMap {

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	public Location getSpawnLocation() {
		return this.getWorld().getSpawnLocation();
	}

	@Override
	public Location getCoverMin() {
		return new Location(this.getWorld(), -8, 67, -9);
	}

	@Override
	public Location getCoverMax() {
		return new Location(this.getWorld(), 8, 67, 8);
	}

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.TAKECOVER_PROTOTYPE;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return "takecover_prototype";
	}

}
