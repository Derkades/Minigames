package derkades.minigames.games.oitq;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

public class Barn extends OITQMap {

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), 0, 65.5, 0);
	}

	@Override
	public String getName() {
		return "Barn";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.OITQ_BARN;
	}

	@Override
	public String getCredits() {
		return "Corrupt_World";
	}

	@Override
	public Size getSize() {
		return Size.LARGE;
	}

	@Override
	public String getIdentifier() {
		return "oneinthequiver_barn";
	}

}
