package derkades.minigames.games.decay;

import org.bukkit.Location;

import derkades.minigames.games.maps.GameMap;
import derkades.minigames.utils.MPlayer;

public abstract class DecayMap extends GameMap {

	public static final DecayMap[] MAPS = {
			new SpruceBrick(),
			new SquareDonut(),
	};

	public abstract Location getSpawnLocation();

	public abstract Location[] getBlocks();

	public abstract boolean isDead(MPlayer player);

}
