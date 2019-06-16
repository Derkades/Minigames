package xyz.derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import xyz.derkades.minigames.games.maps.GameMap;

public abstract class ParkourMap implements GameMap {

	public static final ParkourMap[] MAPS = {
			new JungleRun(),
			//new Plains(),
			new RedstoneCave(),
			new Snow(),
	};

	public abstract Location getStartLocation();

	public abstract Location getSpectatorLocation();

	public abstract boolean hasFinished(Player player, Material blockType);

	public abstract boolean hasDied(Player player, Material blockType);

	public abstract boolean spectatorFreeFlight();

	public abstract int getDuration();

	public void onPlayerFinish(final Player player) {}

}
