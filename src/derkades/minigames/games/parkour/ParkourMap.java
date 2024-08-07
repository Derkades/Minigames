package derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import derkades.minigames.games.GameMap;
import derkades.minigames.utils.MPlayer;

abstract class ParkourMap extends GameMap {

	static final ParkourMap[] MAPS = {
			new JungleRun(),
			//new Plains(),
			//new RedstoneCave(),
			new Snow(),
			new TNT(),
	};

	abstract Location getStartLocation();

	abstract Location getSpectatorLocation();

	abstract boolean hasFinished(MPlayer player, Material blockType);

	abstract boolean hasDied(MPlayer player, Material blockType);

	abstract boolean allowSpectatorFreeFlight();

	abstract int getDuration();

	void onPlayerFinish(final Player player) {}

}
