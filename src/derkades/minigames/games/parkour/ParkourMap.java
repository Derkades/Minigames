package derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import derkades.minigames.games.GameMap;
import derkades.minigames.utils.MPlayer;

abstract class ParkourMap extends GameMap {

	abstract Location getStartLocation();

	abstract Location getSpectatorLocation();

	abstract boolean hasFinished(MPlayer player, Material blockType);

	abstract boolean hasDied(MPlayer player, Material blockType);

	abstract boolean allowSpectatorFreeFlight();

	void onPlayerFinish(final Player player) {}

}
