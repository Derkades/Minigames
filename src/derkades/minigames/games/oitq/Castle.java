package derkades.minigames.games.oitq;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

class Castle extends OITQMap {

	@Override
	public String getName() {
		return "Castle";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.OITQ_CASTLE;
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
	public String getIdentifier() {
		return "oneinthequiver_castle";
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), 0, 65.5, 0);
	}

}
