package xyz.derkades.minigames.games.harvest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.worlds.GameWorld;

public class Prototype extends HarvestMap {

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public MapSize getSize() {
		return null;
	}

	@Override
	public void restoreMap() {
		BlockUtils.fillArea(308, 69, 256, 326, 69, 238, Material.WHEAT, (b) -> ((Ageable) b).setAge(((Ageable) b).getMaximumAge()));
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

}
