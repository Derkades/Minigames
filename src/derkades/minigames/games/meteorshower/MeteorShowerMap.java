package derkades.minigames.games.meteorshower;

import derkades.minigames.games.GameMap;
import org.bukkit.Location;

abstract class MeteorShowerMap extends GameMap {

	abstract MeteorBounds getMeteorBounds();

	abstract Location getSpawnLocation();

}
