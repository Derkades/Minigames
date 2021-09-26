package derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class RedstoneCave extends ParkourMap {

	@Override
	public @NotNull String getName() {
		return "Redstone Cave";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.PARKOUR_REDSTONECAVE;
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
		return "parkour_redstonecave";
	}

	@Override
	Location getStartLocation() {
		return new Location(this.getWorld(), 7.5, 68, -7.5, -180f, 0f);
	}

	@Override
	Location getSpectatorLocation() {
		return null;
	}

	@Override
	boolean hasFinished(final MPlayer player, final Material blockType) {
		return blockType.equals(Material.DIAMOND_BLOCK);
	}

	@Override
	boolean hasDied(final MPlayer player, final Material blockType) {
		return blockType.equals(Material.STONE) ||
				blockType.equals(Material.COBBLESTONE) ||
				blockType.equals(Material.ANDESITE) ||
				blockType.equals(Material.REDSTONE_ORE) ||
				blockType.equals(Material.IRON_ORE) ||
				blockType.equals(Material.COAL_ORE) ||
				player.getLocation().getBlock().getType().equals(Material.WATER);
	}

	@Override
	boolean allowSpectatorFreeFlight() {
		return true;
	}

	@Override
	int getDuration() {
		return 160;
	}

}
