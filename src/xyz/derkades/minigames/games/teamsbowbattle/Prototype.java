package xyz.derkades.minigames.games.teamsbowbattle;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;

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

}
