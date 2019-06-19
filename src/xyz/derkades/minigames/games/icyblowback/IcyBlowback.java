package xyz.derkades.minigames.games.icyblowback;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class IcyBlowback extends IcyBlowbackMap {

	@Override
	public Location[] getSpawnLocations() {
		return new Location[] {
			new Location(this.getWorld(), 17, 38, 17, 135f, 0f),
			new Location(this.getWorld(), 17, 38, -16, 45f, 0f),
			new Location(this.getWorld(), -16, 38, -16, -45, 0f),
			new Location(this.getWorld(), -16, 38, 17, -135f, 0f),
		};
	}

	@Override
	public String getName() {
		return "Icy Blowback";
	}

	@Override
	public MapSize getSize() {
		return MapSize.LARGE;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.ICYBLOWBACK_ICYBLOWBACK;
	}

}
