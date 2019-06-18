package xyz.derkades.minigames.games.sniper;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Desert extends SniperMap {

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

}
