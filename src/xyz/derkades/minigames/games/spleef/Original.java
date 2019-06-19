package xyz.derkades.minigames.games.spleef;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.worlds.GameWorld;

public class Original extends SpleefMap {

	@Override
	public Location getStartLocation() {
		return new Location(this.getWorld(), 0, 65, 0);
	}

	@Override
	public Location getSpectatorLocation() {
		return null; // TP the player upward like 3-5 blocks
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
	public MapSize getSize() {
		return MapSize.SMALL;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.SPLEEF_ORIGINAL;
	}

}
