package xyz.derkades.minigames.games.tntrun;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.BlockUtils;

public class Aqua extends TNTMap {

	@Override
	public void restore() {
		BlockUtils.fillArea(261, 83, 98, 273, 83, 110, Material.PACKED_ICE);
		BlockUtils.fillArea(261, 78, 98, 273, 78, 110, Material.PACKED_ICE);
		BlockUtils.fillArea(261, 73, 98, 273, 73, 110, Material.PACKED_ICE);
		BlockUtils.fillArea(261, 68, 98, 273, 68, 110, Material.PACKED_ICE);
		BlockUtils.fillArea(261, 63, 98, 273, 63, 110, Material.PACKED_ICE);
		
	}

	@Override
	public Material floorMaterial() {
		return Material.PACKED_ICE;
	}

	@Override
	public Location spawnLocation() {
		return new Location(Var.WORLD, 267.5, 85, 104.615);
	}

}