package xyz.derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.worlds.GameWorld;

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
		return new Location(this.getWorld(), 7.5, 68, -7.5, -180f, 0f);
	}

	@Override
	public Location getSpectatorLocation() {
		return null;
	}

	@Override
	public boolean hasFinished(final MPlayer player, final Material blockType) {
		return blockType.equals(Material.DIAMOND_BLOCK);
	}

	@Override
	public boolean hasDied(final MPlayer player, final Material blockType) {
		return blockType.equals(Material.STONE) ||
				blockType.equals(Material.COBBLESTONE) ||
				blockType.equals(Material.ANDESITE) ||
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

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.PARKOUR_REDSTONECAVE;
	}

}
