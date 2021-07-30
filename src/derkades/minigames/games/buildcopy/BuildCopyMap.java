package derkades.minigames.games.buildcopy;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.games.maps.GameMap;

public abstract class BuildCopyMap extends GameMap {

	public static final BuildCopyMap[] MAPS = {
			new Prototype(),
	};

	public abstract int getSupportedPlayerCount();

	public abstract Location getSpawnLocation(int position);

	public abstract void buildOriginal(int position, Material[] materials);

	public abstract void clearCopy(int position);

	public abstract boolean checkCopy(int position, Material[] materials);

}
