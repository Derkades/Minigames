package xyz.derkades.minigames.tntrun;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.BlockUtils;

public class Jungle extends TNTMap {

	@Override
	public void restore() {
		BlockUtils.fillArea(161, 83, 138, 273, 83, 150, Material.TNT);
		BlockUtils.fillArea(161, 78, 138, 273, 78, 150, Material.TNT);
		BlockUtils.fillArea(161, 73, 138, 273, 73, 150, Material.TNT);
		BlockUtils.fillArea(161, 68, 138, 273, 68, 150, Material.TNT);
		BlockUtils.fillArea(161, 63, 138, 273, 63, 150, Material.TNT);
	}

	@Override
	public Material floorMaterial() {
		return Material.TNT;
	}

	@Override
	public Location spawnLocation() {
		return new Location(Var.WORLD, 267.5, 85, 144.5);
	}

}
