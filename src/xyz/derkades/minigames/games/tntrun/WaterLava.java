package xyz.derkades.minigames.games.tntrun;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.derkutils.bukkit.BlockUtils;
import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class WaterLava extends TNTMap {

	@Override
	public void restore() {
		BlockUtils.fillArea(this.getWorld(), 5, 64, -5, -5, 64, 5, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), 5, 59, -5, -5, 59, 5, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), 5, 54, -5, -5, 54, 5, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), 5, 49, -5, -5, 49, 5, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), 5, 44, -5, -5, 44, 5, this.floorMaterial());
	}

	@Override
	public Material floorMaterial() {
		return Material.COBBLESTONE;
	}

	@Override
	public Location spawnLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

	@Override
	public String getName() {
		return "Water-Lava";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.TNTRUN_WATERLAVA;
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
		return "tntrun_waterlava";
	}

}
