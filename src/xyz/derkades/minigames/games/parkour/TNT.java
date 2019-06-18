package xyz.derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

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
		return new Location(this.getWorld(), -18.5, 65, 5.5, -45.0f, 0.0f);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(this.getWorld(), -16.5, 71, 16.5, -135f, 25f);
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

	@Override
	public GameWorld getGameWorld() {
		return null;
	}

}
