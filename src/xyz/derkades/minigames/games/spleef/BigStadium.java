package xyz.derkades.minigames.games.spleef;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.BlockUtils;

public class BigStadium extends SpleefMap {

	@Override
	public Location getStartLocation() {
		return new Location(Var.WORLD, 67, 91, 143);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 67, 97, 143);
	}

	@Override
	public void fill() {
		BlockUtils.fillArea(45, 89, 131, 89, 89, 155, Material.SNOW_BLOCK);
	}

	@Override
	public String getName() {
		return "Big Stadium";
	}

}
