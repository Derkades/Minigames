package xyz.derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;

public class Snow extends ParkourMap {

	@Override
	public String getName() {
		return "Snow";
	}

	@Override
	public MapSize getSize() {
		return null;
	}

	@Override
	public Location getStartLocation() {
		return new Location(Var.WORLD, 277.5, 70, 273.5, -90, 0);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 320, 81, 274, 90, 0);
	}

	@Override
	public boolean hasFinished(Player player, Material blockType) {
		return blockType == Material.DIAMOND_BLOCK;
	}

	@Override
	public boolean hasDied(Player player, Material blockType) {
		return blockType == Material.SNOW || blockType == Material.ICE;
	}

	@Override
	public boolean spectatorFreeFlight() {
		return false;
	}

	@Override
	public int getDuration() {
		return 30;
	}

}
