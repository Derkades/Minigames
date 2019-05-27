package xyz.derkades.minigames.games.tron;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.Tron.TronTeam;
import xyz.derkades.minigames.games.maps.MapSize;

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
		return new Location(Var.WORLD, 445, 83, 119);
	}

	@Override
	public Location getOuterCornerTwo() {
		return new Location(Var.WORLD, 385, 83, 39);
	}

	@Override
	public Location getInnerCornerOne() {
		return new Location(Var.WORLD, 444, 83, 118);
	}

	@Override
	public Location getInnerCornerTwo() {
		return new Location(Var.WORLD, 386, 83, 40);
	}

	@Override
	public Location getSpectatorSpawnLocation() {
		return new Location(Var.WORLD, 420, 105, 78);
	}

	@Override
	public Map<TronTeam, Location> getSpawnLocations() {
		final Map<TronTeam, Location> map = new HashMap<>();
		map.put(TronTeam.ORANGE, new Location(Var.WORLD, 443, 84, 117));
		map.put(TronTeam.PURPLE, new Location(Var.WORLD, 387, 84, 117));
		map.put(TronTeam.LIGHT_BLUE, new Location(Var.WORLD, 388, 84, 41));
		map.put(TronTeam.YELLOW, new Location(Var.WORLD, 442, 84, 42));
		map.put(TronTeam.GREEN, new Location(Var.WORLD, 415, 84, 41));
		map.put(TronTeam.PINK, new Location(Var.WORLD, 443, 84, 79));
		map.put(TronTeam.LIME, new Location(Var.WORLD, 415, 84, 117));
		map.put(TronTeam.GRAY, new Location(Var.WORLD, 387, 84, 79));
		map.put(TronTeam.WHITE, new Location(Var.WORLD, 415, 84, 63));
		map.put(TronTeam.BLUE, new Location(Var.WORLD, 415, 84, 95));

		return map;
	}


}
