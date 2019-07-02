package xyz.derkades.minigames.games.elytra;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.utils.MPlayer;

public abstract class ElytraMap extends GameMap {

	public static final ElytraMap[] MAPS = {
			new Cave(),
	};

	public abstract boolean isDead(MPlayer player);

	public abstract boolean hasFinished(MPlayer player);

	public abstract Location getStartLocation();

}
