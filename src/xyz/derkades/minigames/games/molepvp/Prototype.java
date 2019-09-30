package xyz.derkades.minigames.games.molepvp;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.derkutils.bukkit.BlockUtils;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class Prototype extends MolePvPMap {

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public void setupMap() {
		// TODO fix molepvp
		BlockUtils.fillArea(Var.WORLD, 244, 67, 161, 224, 74, 142, Material.DIRT);
		BlockUtils.fillArea(Var.WORLD, 244, 69, 161, 242, 67, 159, Material.AIR);
		BlockUtils.fillArea(Var.WORLD, 226, 74, 144, 224, 72, 142, Material.AIR);
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
	public GameWorld getGameWorld() {
		return null;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return "molepvp_prototype";
	}

}
