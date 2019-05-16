package xyz.derkades.minigames.games.spleef;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.BlockUtils;

public class LittleStadium extends SpleefMap {

	@Override
	public Location getStartLocation() {
		return new Location(Var.WORLD, 358, 81, 235);
	}
	
	@Override
	public Location getSpectatorLocation() {
		if (Random.getRandomBoolean()) {
			return new Location(Var.WORLD, 358.0, 82, 224.5, 0, 0);
		} else {
			return new Location(Var.WORLD, 358.0, 82, 246.5, -180, 0);
		}		
	}
	
	@Override
	public void fill() {
		BlockUtils.fillArea(348, 79, 230, 367, 79, 240, Material.SNOW_BLOCK);
	}

	@Override
	public String getName() {
		return "Little Stadium";
	}
	
}