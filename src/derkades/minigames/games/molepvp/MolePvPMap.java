package derkades.minigames.games.molepvp;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;

abstract class MolePvPMap extends GameMap {

	static final MolePvPMap[] MAPS = {
			new Prototype(),
	};

	abstract void setUpMap();

	abstract Location getTeamRedSpawnLocation();

	abstract Location getTeamBlueSpawnLocation();


}
