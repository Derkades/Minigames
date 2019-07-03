package xyz.derkades.minigames.games.oitq;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Desert extends OITQMap {

	@Override
	public String getName() {
		return "Desert";
	}

	@Override
	public MapSize getSize() {
		return MapSize.LARGE;
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), -27, 72, -4);
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.OITQ_DESERT;
	}

	@Override
	public String getCredits() {
		return "funlolxxl";
	}

}
