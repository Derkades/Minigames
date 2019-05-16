package xyz.derkades.minigames.games.spleef;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.BlockUtils;

public class Original extends SpleefMap {

	@Override
	public Location getStartLocation() {
		return new Location(Var.WORLD, 156.5, 82, 260.5, -90, 90);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 156.5, 89, 260.5, -90, 90);
	}

	@Override
	public void fill() {
		BlockUtils.fillArea(149, 80, 253, 163, 80, 267, Material.SNOW_BLOCK);
	}

	@Override
	public String getName() {
		return "Original";
	}

}
