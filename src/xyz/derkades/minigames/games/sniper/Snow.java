package xyz.derkades.minigames.games.sniper;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Snow extends SniperMap {

	@Override
	public String getName() {
		return "Snow";
	}

	@Override
	public MapSize getSize() {
		return MapSize.NORMAL;
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), -22, 66, -1);
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.OITQ_SNOW;
	}

}
