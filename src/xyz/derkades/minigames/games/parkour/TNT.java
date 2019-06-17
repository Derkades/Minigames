package xyz.derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;

public class TNT extends ParkourMap {

	@Override
	public String getName() {
		return "TNT";
	}

	@Override
	public MapSize getSize() {
		return MapSize.NORMAL;
	}

	@Override
	public Location getStartLocation() {
		return new Location(Var.WORLD, 255.5, 40, 143.5);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 228.5, 46, 155.5);
	}

	@Override
	public boolean hasFinished(final Player player, final Material blockType) {
		return blockType.equals(Material.GREEN_TERRACOTTA);
	}

	@Override
	public boolean hasDied(final Player player, final Material blockType) {
		return blockType.equals(Material.RED_CONCRETE) || blockType.equals(Material.REDSTONE_BLOCK) ||
				blockType.equals(Material.FURNACE);
	}

	@Override
	public boolean spectatorFreeFlight() {
		return true;
	}

	@Override
	public int getDuration() {
		return 60;
	}

}
