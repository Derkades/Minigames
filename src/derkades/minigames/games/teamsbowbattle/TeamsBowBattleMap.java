package derkades.minigames.games.teamsbowbattle;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

public abstract class TeamsBowBattleMap extends GameMap {

	public static final TeamsBowBattleMap[] MAPS = {
			new Forest(),
	};

	public abstract Location getTeamRedSpawnLocation();

	public abstract Location getTeamBlueSpawnLocation();

	public void onGameStart() {}

}
