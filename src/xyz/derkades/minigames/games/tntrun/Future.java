package xyz.derkades.minigames.games.tntrun;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.BlockUtils;

public class Future extends TNTMap {

	@Override
	public void restore() {
		BlockUtils.fillArea(261, 80, 119, 273, 80, 131, Material.IRON_BLOCK);
		BlockUtils.fillArea(261, 74, 119, 273, 74, 131, Material.IRON_BLOCK);
		BlockUtils.fillArea(261, 68, 119, 273, 68, 131, Material.IRON_BLOCK);
		BlockUtils.fillArea(261, 62, 119, 273, 62, 131, Material.IRON_BLOCK);
	}

	@Override
	public Material floorMaterial() {
		return Material.IRON_BLOCK;
	}

	@Override
	public Location spawnLocation() {
		return new Location(Var.WORLD, 267.5, 82, 125.5);
	}

}
