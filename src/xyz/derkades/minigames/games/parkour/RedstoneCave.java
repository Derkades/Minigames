package xyz.derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;

public class RedstoneCave extends ParkourMap {

	@Override
	public String getName() {
		return "Redstone Cave";
	}

	@Override
	public MapSize getSize() {
		return MapSize.NORMAL;
	}

	@Override
	public Location getStartLocation() {
		return new Location(Var.WORLD, 273.5, 17, 196.5, -170f, 2.5f);
	}

	@Override
	public Location getSpectatorLocation() {
		return this.getStartLocation();
	}

	@Override
	public boolean hasFinished(final Player player, final Material blockType) {
		return blockType.equals(Material.DIAMOND_BLOCK);
	}

	@Override
	public boolean hasDied(final Player player, final Material blockType) {
		return blockType.equals(Material.STONE) ||
				blockType.equals(Material.COBBLESTONE) ||
				blockType.equals(Material.REDSTONE_ORE) ||
				blockType.equals(Material.IRON_ORE) ||
				blockType.equals(Material.COAL_ORE) ||
				player.getLocation().getBlock().getType().equals(Material.WATER);
	}

	@Override
	public boolean spectatorFreeFlight() {
		return true;
	}

	@Override
	public int getDuration() {
		return 160;
	}

}
