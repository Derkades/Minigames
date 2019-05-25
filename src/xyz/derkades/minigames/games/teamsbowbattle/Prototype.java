package xyz.derkades.minigames.games.teamsbowbattle;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;

public class Prototype extends TeamsBowBattleMap {

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public Location getTeamRedSpawnLocation() {
		return new Location(Var.WORLD, 402.5, 72, 325.5);
	}

	@Override
	public Location getTeamBlueSpawnLocation() {
		return new Location(Var.WORLD, 402.5, 72, 298.5);
	}

	@Override
	public MapSize getSize() {
		return MapSize.NORMAL;
	}

}
