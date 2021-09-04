package derkades.minigames.games.teamsbowbattle;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class TeamsBowBattleMap extends GameMap {

	static final TeamsBowBattleMap[] MAPS = {
			new Forest(),
	};

	abstract Location getTeamRedSpawnLocation();

	abstract Location getTeamBlueSpawnLocation();

//	void onGameStart() {}

}
