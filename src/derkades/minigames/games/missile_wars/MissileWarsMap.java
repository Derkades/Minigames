package derkades.minigames.games.missile_wars;

import org.bukkit.Location;

import derkades.minigames.games.maps.GameMap;

public abstract class MissileWarsMap extends GameMap {

	public static final MissileWarsMap[] MAPS = {
			new MissileWars(),
	};

	public abstract void buildArena();

	public abstract Location getArenaBorderMin();

	public abstract Location getArenaBorderMax();

	public abstract Location getTeamRedSpawnLocation();

	public abstract Location getTeamBlueSpawnLocation();

}
