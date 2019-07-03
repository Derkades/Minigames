package xyz.derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.worlds.GameWorld;

public class TNT extends ParkourMap {

	@Override
	public String getName() {
		return "TNT";
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
	public boolean hasFinished(final MPlayer player, final Material blockType) {
		return blockType.equals(Material.GREEN_TERRACOTTA);
	}

	@Override
	public boolean hasDied(final MPlayer player, final Material blockType) {
		return blockType.equals(Material.RED_CONCRETE) || blockType.equals(Material.REDSTONE_BLOCK) ||
				blockType.equals(Material.FURNACE);
	}

	@Override
	public boolean spectatorFreeFlight() {
		return true;
	}

	@Override
	public int getDuration() {
		return 80;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.PARKOUR_TNT;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return "parkour_tnt";
	}

}
