package xyz.derkades.minigames.games.icyblowback;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;

public class IcyBlowback extends IcyBlowbackMap {

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, -17.5, 95.5, 88.5);
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
	
}