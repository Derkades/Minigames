package xyz.derkades.minigames.games.teamsbowbattle;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class TeamsBowBattleMap implements GameMap {
	
	public static final TeamsBowBattleMap[] MAPS = {
			new Prototype(),
	};

	public abstract Location getTeamRedSpawnLocation();
	
	public abstract Location getTeamBlueSpawnLocation();

}
