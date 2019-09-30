package xyz.derkades.minigames.games.harvest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;

import xyz.derkades.derkutils.bukkit.BlockUtils;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class Prototype extends HarvestMap {

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public void restoreMap() {
		BlockUtils.fillArea(Var.WORLD, 308, 69, 256, 326, 69, 238, Material.WHEAT, (b) -> ((Ageable) b).setAge(((Ageable) b).getMaximumAge()));
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(Var.WORLD, 327, 71, 248);
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
		return Size.SMALL;
	}

	@Override
	public String getIdentifier() {
		return "harvest_prototype";
	}

}
