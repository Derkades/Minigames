package derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import derkades.minigames.games.maps.GameMap;
import derkades.minigames.utils.MPlayer;

public abstract class ParkourMap extends GameMap {

	public static final ParkourMap[] MAPS = {
			new JungleRun(),
			//new Plains(),
			//new RedstoneCave(),
			new Snow(),
			new TNT(),
	};

	public abstract Location getStartLocation();

	public abstract Location getSpectatorLocation();

	public abstract boolean hasFinished(MPlayer player, Material blockType);

	public abstract boolean hasDied(MPlayer player, Material blockType);

	public abstract boolean spectatorFreeFlight();

	public abstract int getDuration();

	public void onPlayerFinish(final Player player) {}

}
