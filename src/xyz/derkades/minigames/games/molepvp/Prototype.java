package xyz.derkades.minigames.games.molepvp;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.worlds.GameWorld;

public class Prototype extends MolePvPMap {

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public void setupMap() {
		// TODO fix molepvp
		BlockUtils.fillArea(244, 67, 161, 224, 74, 142, Material.DIRT); 
		BlockUtils.fillArea(244, 69, 161, 242, 67, 159, Material.AIR);
		BlockUtils.fillArea(226, 74, 144, 224, 72, 142, Material.AIR);
	}

	@Override
	public Location getTeamRedSpawnLocation() {
		return new Location(Var.WORLD, 243.5, 67.5, 160.5);
	}

	@Override
	public Location getTeamBlueSpawnLocation() {
		return new Location(Var.WORLD, 225.5, 72, 143.5);
	}

	@Override
	public MapSize getSize() {
		return MapSize.NORMAL;
	}

	@Override
	public GameWorld getGameWorld() {
		return null;
	}

}
