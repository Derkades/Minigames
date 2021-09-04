package derkades.minigames.games.spleef;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import xyz.derkades.derkutils.bukkit.BlockUtils;

class BigStadium extends SpleefMap {

	@Override
	public String getName() {
		return "Big Stadium";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.SPLEEF_BIGSTADIUM;
	}

	@Override
	public String getCredits() {
		return "MissChikoo";
	}

	@Override
	public Size getSize() {
		return Size.LARGE;
	}

	@Override
	public String getIdentifier() {
		return "spleef_bigstadium";
	}

	@Override
	Location getStartLocation() {
		return new Location(this.getWorld(), 0, 65, 0);
	}

	@Override
	void fill() {
		BlockUtils.fillArea(this.getWorld(), 22, 64, -12, -22, 64, 13, Material.SNOW_BLOCK);
	}

	@Override
	boolean enableFlyingBlocks() {
		return false;
	}

}
