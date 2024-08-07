package derkades.minigames.games.breaktheblock;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;
import derkades.minigames.utils.MPlayer;

public abstract class BreakTheBlockMap extends GameMap {

	static final BreakTheBlockMap[] MAPS = {
			new Cake(),
			new HollowHills(),
//			new Jungle(),
			new Prototype(),
	};

	abstract Location[] getStartLocations();

	abstract boolean canTakeDamage(MPlayer player);

	int getMinimumY() {
		return 60;
	}

}
