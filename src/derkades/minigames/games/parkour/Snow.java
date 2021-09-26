package derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class Snow extends ParkourMap {

	@Override
	public @NotNull String getName() {
		return "Snow";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.PARKOUR_SNOW;
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
		return "parkour_snow";
	}

	@Override
	Location getStartLocation() {
		return new Location(this.getWorld(), 6.5, 50, 0.5, -90, 0);
	}

	@Override
	Location getSpectatorLocation() {
		return new Location(this.getWorld(), 31, 58, 4);
	}

	@Override
	boolean hasFinished(final MPlayer player, final Material blockType) {
		return blockType == Material.DIAMOND_BLOCK;
	}

	@Override
	boolean hasDied(final MPlayer player, final Material blockType) {
		return blockType == Material.SNOW_BLOCK || blockType == Material.ICE;
	}

	@Override
	boolean allowSpectatorFreeFlight() {
		return true;
	}

	@Override
	int getDuration() {
		return 70;
	}

}
