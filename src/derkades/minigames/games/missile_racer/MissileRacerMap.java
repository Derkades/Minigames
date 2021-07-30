package derkades.minigames.games.missile_racer;

import org.bukkit.Location;

import derkades.minigames.games.maps.GameMap;
import derkades.minigames.utils.MPlayer;

public abstract class MissileRacerMap extends GameMap {

	public static final MissileRacerMap[] MAPS = {
			new Prototype(),
	};

	public abstract Location getSpawnLocation();

	public abstract boolean isInFinishBounds(MPlayer player);

	public abstract int getMinimumY();

}
