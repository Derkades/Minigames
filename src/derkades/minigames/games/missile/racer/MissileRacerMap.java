package derkades.minigames.games.missile.racer;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;
import derkades.minigames.utils.MPlayer;

abstract class MissileRacerMap extends GameMap {

	static final MissileRacerMap[] MAPS = {
			new Prototype(),
	};

	abstract Location getSpawnLocation();

	abstract boolean isInFinishBounds(MPlayer player);

	abstract int getMinimumY();

}
