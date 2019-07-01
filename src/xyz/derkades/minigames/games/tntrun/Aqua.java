package xyz.derkades.minigames.games.tntrun;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.worlds.GameWorld;

public class Aqua extends TNTMap {

	@Override
	public void restore() {
		BlockUtils.fillArea(this.getWorld(), 12, 64, -6, -12, 64, 6, Material.PACKED_ICE);
		BlockUtils.fillArea(this.getWorld(), 12, 59, -6, -12, 59, 6, Material.PACKED_ICE);
		BlockUtils.fillArea(this.getWorld(), 12, 54, -6, -12, 54, 6, Material.PACKED_ICE);
		BlockUtils.fillArea(this.getWorld(), 12, 49, -6, -12, 49, 6, Material.PACKED_ICE);
		BlockUtils.fillArea(this.getWorld(), 12, 45, -6, -12, 45, 6, Material.PACKED_ICE);
	}

	@Override
	public Material floorMaterial() {
		return Material.PACKED_ICE;
	}

	@Override
	public Location spawnLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

	@Override
	public String getName() {
		return "Aqua";
	}

	@Override
	public MapSize getSize() {
		return MapSize.LARGE;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.TNTRUN_AQUA;
	}

	@Override
	public String getCredits() {
		return null;
	}

}
