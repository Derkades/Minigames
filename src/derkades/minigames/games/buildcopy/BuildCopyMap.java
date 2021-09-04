package derkades.minigames.games.buildcopy;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.games.maps.GameMap;

abstract class BuildCopyMap extends GameMap {

	static final BuildCopyMap[] MAPS = {
			new Prototype(),
	};

	abstract int getSupportedPlayerCount();

	abstract Location getSpawnLocation(int position);

	abstract void buildOriginal(int position, Material[] materials);

	abstract void clearCopy(int position);

	abstract boolean checkCopy(int position, Material[] materials);

}
