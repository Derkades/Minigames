package derkades.minigames.games.teamsbowbattle;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class TeamsBowBattleMap extends GameMap {

	abstract Location getTeamRedSpawnLocation();

	abstract Location getTeamBlueSpawnLocation();

}
