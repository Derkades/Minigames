package xyz.derkades.minigames.games.tron;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import xyz.derkades.minigames.games.Tron.TronTeam;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Prototype extends TronMap {

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public MapSize getSize() {
		return MapSize.LARGE;
	}

	@Override
	public Location getOuterCornerOne() {
		return new Location(this.getWorld(), 30, 65, 40);
	}

	@Override
	public Location getOuterCornerTwo() {
		return new Location(this.getWorld(), -30, 65, -40);
	}

	@Override
	public Location getInnerCornerOne() {
		return new Location(this.getWorld(), 29, 64, 39);
	}

	@Override
	public Location getInnerCornerTwo() {
		return new Location(this.getWorld(), -29, 64, -39);
	}

	@Override
	public Map<TronTeam, Location> getSpawnLocations() {
		final Map<TronTeam, Location> map = new HashMap<>();
		map.put(TronTeam.LIGHT_BLUE, new Location(this.getWorld(), -28, 64, -38));	// 1
		map.put(TronTeam.LIME, new Location(this.getWorld(), 28, 64, -38));			// 2
		map.put(TronTeam.ORANGE, new Location(this.getWorld(), -28, 64, 38));		// 3
		map.put(TronTeam.RED, new Location(this.getWorld(), 28, 64, 38));			// 4
		map.put(TronTeam.PURPLE, new Location(this.getWorld(), 28, 64, 0));			// 5
		map.put(TronTeam.BLUE, new Location(this.getWorld(), -28, 64, 0));			// 6
		map.put(TronTeam.PINK, new Location(this.getWorld(), 14, 64, 20));			// 7
		map.put(TronTeam.YELLOW, new Location(this.getWorld(), -14, 64, 20));		// 8
		map.put(TronTeam.GREEN, new Location(this.getWorld(), 14, 64, -20));		// 9
		map.put(TronTeam.WHITE, new Location(this.getWorld(), -14, 64, -20));		// 10
		return map;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.TRON_PROTOTYPE;
	}


}
