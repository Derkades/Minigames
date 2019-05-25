package xyz.derkades.minigames.games.icyblowback;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;

public class IcyBlowback extends IcyBlowbackMap {

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, -18.5, 116, 88.5);
	}
	
	@Override
	public Location[] getSpawnLocations() {
		return new Location[] {
			new Location(Var.WORLD, -35, 89, 105.5),
			new Location(Var.WORLD, -35.5, 89, 71.5),
			new Location(Var.WORLD, -1.5, 89, 71.5),
			new Location(Var.WORLD, -1.5, 89, 105.5),
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
	
}
