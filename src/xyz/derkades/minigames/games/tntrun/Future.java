package xyz.derkades.minigames.games.tntrun;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.worlds.GameWorld;

public class Future extends TNTMap {

	@Override
	public void restore() {
		BlockUtils.fillArea(this.getWorld(), -6, 64, 6, 6, 64, -6, Material.IRON_BLOCK);
		BlockUtils.fillArea(this.getWorld(), -6, 58, 6, 6, 58, -6, Material.IRON_BLOCK);
		BlockUtils.fillArea(this.getWorld(), -6, 52, 6, 6, 52, -6, Material.IRON_BLOCK);
		BlockUtils.fillArea(this.getWorld(), -6, 46, 6, 6, 46, -6, Material.IRON_BLOCK);
	}

	@Override
	public Material floorMaterial() {
		return Material.IRON_BLOCK;
	}

	@Override
	public Location spawnLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

	@Override
	public String getName() {
		return "Future";
	}

	@Override
	public MapSize getSize() {
		return MapSize.SMALL;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.TNTRUN_FUTURE;
	}

}
