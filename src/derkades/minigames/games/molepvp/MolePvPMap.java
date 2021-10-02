package derkades.minigames.games.molepvp;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class MolePvPMap extends GameMap {

	abstract void setUpMap();

	abstract Location getTeamRedSpawnLocation();

	abstract Location getTeamBlueSpawnLocation();


}
