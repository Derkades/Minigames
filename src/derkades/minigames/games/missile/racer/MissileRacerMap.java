package derkades.minigames.games.missile.racer;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;
import derkades.minigames.utils.MPlayer;

abstract class MissileRacerMap extends GameMap {

	abstract Location getSpawnLocation();

	abstract boolean isInFinishBounds(MPlayer player);

	abstract int getMinimumY();

}
