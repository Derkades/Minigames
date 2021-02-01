package derkades.minigames.games.spleef;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import xyz.derkades.derkutils.bukkit.BlockUtils;

public class BigStadium extends SpleefMap {

	@Override
	public Location getStartLocation() {
		return new Location(this.getWorld(), 0, 65, 0);
	}

	@Override
	public void fill() {
		BlockUtils.fillArea(this.getWorld(), 22, 64, -12, -22, 64, 13, Material.SNOW_BLOCK);
	}

	@Override
	public String getName() {
		return "Big Stadium";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.SPLEEF_BIGSTADIUM;
	}

	@Override
	public boolean enableFlyingBlocks() {
		return false;
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

}
