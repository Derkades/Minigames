package xyz.derkades.minigames.games.decay;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.utils.MPlayer;

public abstract class DecayMap extends GameMap {
	
	public static final DecayMap[] MAPS = {
			new SpruceBrick(),
	};
	
	public abstract Location getSpawnLocation();
	
	public abstract Location[] getBlocks();
	
	public abstract boolean isDead(MPlayer player);

}
