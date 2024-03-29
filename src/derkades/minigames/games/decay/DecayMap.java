package derkades.minigames.games.decay;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;
import derkades.minigames.utils.MPlayer;

public abstract class DecayMap extends GameMap {

	abstract Location getSpawnLocation();

	abstract Location[] getBlocks();

	abstract boolean isDead(MPlayer player);

}
