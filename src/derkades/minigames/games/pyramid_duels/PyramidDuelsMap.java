package derkades.minigames.games.pyramid_duels;

import derkades.minigames.games.GameMap;
import org.bukkit.Location;

public abstract class PyramidDuelsMap extends GameMap {

	public abstract PyramidNode getRootNode();

	public abstract Location getSpectatorLocation();

}
