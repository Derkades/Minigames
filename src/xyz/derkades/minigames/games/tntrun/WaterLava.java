package xyz.derkades.minigames.games.tntrun;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.BlockUtils;

public class WaterLava extends TNTMap {

	@Override
	public void restore() {
		BlockUtils.fillArea(262, 84, 79, 272, 84, 89, Material.TNT);
		BlockUtils.fillArea(262, 79, 79, 272, 79, 89, Material.TNT);
		BlockUtils.fillArea(262, 74, 79, 272, 74, 89, Material.TNT);
		BlockUtils.fillArea(262, 69, 79, 272, 69, 89, Material.TNT);
		BlockUtils.fillArea(262, 64, 79, 272, 64, 89, Material.TNT);
	}

	@Override
	public Material floorMaterial() {
		return Material.COBBLESTONE;
	}

	@Override
	public Location spawnLocation() {
		return new Location(Var.WORLD, 267, 85, 84.5);
	}

}
