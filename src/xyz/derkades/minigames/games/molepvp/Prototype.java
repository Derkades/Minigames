package xyz.derkades.minigames.games.molepvp;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.BlockUtils;

public class Prototype extends MolePvPMap {

	@Override
	public String getName() {
		return "Prototype";
	}
	
	@Override
	public void setupMap() {
		BlockUtils.fillArea(244, 67, 161, 212, 74, 131, Material.DIRT);
		BlockUtils.fillArea(244, 69, 161, 242, 67, 159, Material.AIR);
		BlockUtils.fillArea(214, 67, 133, 212, 69, 131, Material.AIR);
	}

	@Override
	public Location getTeamRedSpawnLocation() {
		return new Location(Var.WORLD, 243.5, 67.5, 160.5);
	}

	@Override
	public Location getTeamBlueSpawnLocation() {
		return new Location(Var.WORLD, 213.5, 67.5, 132.5);
	}

}
