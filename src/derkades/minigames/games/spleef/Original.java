package derkades.minigames.games.spleef;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import xyz.derkades.derkutils.bukkit.BlockUtils;

public class Original extends SpleefMap {

	@Override
	public Location getStartLocation() {
		return new Location(this.getWorld(), 0, 65, 0);
	}

	@Override
	public void fill() {
		BlockUtils.fillArea(this.getWorld(), -7, 64, -7, 7, 64, 7, Material.SNOW_BLOCK);
	}

	@Override
	public String getName() {
		return "Original";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.SPLEEF_ORIGINAL;
	}

	@Override
	public boolean enableFlyingBlocks() {
		return true;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return Size.SMALL;
	}

	@Override
	public String getIdentifier() {
		return "spleef_original";
	}

}
