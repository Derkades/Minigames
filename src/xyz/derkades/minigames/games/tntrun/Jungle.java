package xyz.derkades.minigames.games.tntrun;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.worlds.GameWorld;

public class Jungle extends TNTMap {

	@Override
	public void restore() {
		BlockUtils.fillArea(261, 83, 138, 273, 83, 150, Material.TNT);
		BlockUtils.fillArea(261, 78, 138, 273, 78, 150, Material.TNT);
		BlockUtils.fillArea(261, 73, 138, 273, 73, 150, Material.TNT);
		BlockUtils.fillArea(261, 68, 138, 273, 68, 150, Material.TNT);
		BlockUtils.fillArea(261, 63, 138, 273, 63, 150, Material.TNT);
	}

	@Override
	public Material floorMaterial() {
		return Material.TNT;
	}

	@Override
	public Location spawnLocation() {
		return new Location(Var.WORLD, 267.5, 85, 144.5);
	}

	@Override
	public String getName() {
		return "Jungle";
	}

	@Override
	public MapSize getSize() {
		return MapSize.SMALL;
	}

	@Override
	public GameWorld getGameWorld() {
		return null;
	}

}
