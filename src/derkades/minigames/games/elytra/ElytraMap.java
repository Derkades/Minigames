package derkades.minigames.games.elytra;

import org.bukkit.Location;

import derkades.minigames.games.GameMap;
import derkades.minigames.utils.MPlayer;

abstract class ElytraMap extends GameMap {

	static final ElytraMap[] MAPS = {
			new Cave(),
	};

	abstract boolean isDead(MPlayer player);

	abstract boolean hasFinished(MPlayer player);

	abstract Location getStartLocation();

}
