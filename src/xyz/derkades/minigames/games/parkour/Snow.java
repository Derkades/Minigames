package xyz.derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.worlds.GameWorld;

public class Snow extends ParkourMap {

	@Override
	public String getName() {
		return "Snow";
	}

	@Override
	public Location getStartLocation() {
		return new Location(this.getWorld(), 6.5, 50, 0.5, -90, 0);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(this.getWorld(), 31, 58, 4);
	}

	@Override
	public boolean hasFinished(final MPlayer player, final Material blockType) {
		return blockType == Material.DIAMOND_BLOCK;
	}

	@Override
	public boolean hasDied(final MPlayer player, final Material blockType) {
		return blockType == Material.SNOW_BLOCK || blockType == Material.ICE;
	}

	@Override
	public boolean spectatorFreeFlight() {
		return true;
	}

	@Override
	public int getDuration() {
		return 70;
	}

	@Override
	public GameWorld getGameWorld() {
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
	public String getIdentifier() {
		return "parkour_snow";
	}

}
