package derkades.minigames.games.studyfall;

import derkades.minigames.games.GameMap;
import org.bukkit.Location;

public abstract class StudyFallMap extends GameMap {

	abstract void clearGlass();

	abstract Location getPreSpawnLocation();

	abstract Location getSpawnLocation();

}
