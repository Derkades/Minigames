package derkades.minigames.games.missile.wars;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

public abstract class MissileWarsMap extends GameMap {

	public abstract void buildArena();

	public abstract Location getArenaBorderMin();

	public abstract Location getArenaBorderMax();

	abstract Location getTeamRedSpawnLocation();

	abstract Location getTeamBlueSpawnLocation();

}
