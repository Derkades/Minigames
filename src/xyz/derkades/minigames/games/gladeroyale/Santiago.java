package xyz.derkades.minigames.games.gladeroyale;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Santiago extends GladeRoyaleMap {

	@Override
	public int getWorldborderSize() {
		return 512;
	}

	@Override
	public Location getMapCenter() {
		return new Location(this.getWorld(), 1374.5, 50.0, 637);
	}

	@Override
	public String getName() {
		return "Santiago";
	}

	@Override
	public MapSize getSize() {
		return null;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.MGR_SANTIAGO;
	}

}
