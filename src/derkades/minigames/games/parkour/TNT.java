package derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class TNT extends ParkourMap {

	@Override
	public @NotNull String getName() {
		return "TNT";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
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
	public @NotNull String getIdentifier() {
		return "parkour_tnt";
	}

	@Override
	Location getStartLocation() {
		return new Location(this.getWorld(), -18.5, 65, 5.5, -45.0f, 0.0f);
	}

	@Override
	Location getSpectatorLocation() {
		return new Location(this.getWorld(), -16.5, 71, 16.5, -135f, 25f);
	}

	@Override
	boolean hasFinished(final MPlayer player, final Material blockType) {
		return blockType.equals(Material.GREEN_TERRACOTTA);
	}

	@Override
	boolean hasDied(final MPlayer player, final Material blockType) {
		return blockType.equals(Material.RED_CONCRETE) || blockType.equals(Material.REDSTONE_BLOCK) ||
				blockType.equals(Material.FURNACE);
	}

	@Override
	boolean allowSpectatorFreeFlight() {
		return true;
	}

}
