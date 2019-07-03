package xyz.derkades.minigames.games.oitq;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Castle extends OITQMap {

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), 0, 65.5, 0);
	}

	@Override
	public String getName() {
		return "Castle";
	}

	@Override
	public MapSize getSize() {
		return null;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.OITQ_CASTLE;
	}

	@Override
	public String getCredits() {
		return null;
	}

}
