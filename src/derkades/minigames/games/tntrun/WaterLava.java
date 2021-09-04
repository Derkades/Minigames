package derkades.minigames.games.tntrun;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import xyz.derkades.derkutils.bukkit.BlockUtils;

class WaterLava extends TntRunMap {

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

	@Override
	void restore() {
		BlockUtils.fillArea(this.getWorld(), 5, 64, -5, -5, 64, 5, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), 5, 59, -5, -5, 59, 5, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), 5, 54, -5, -5, 54, 5, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), 5, 49, -5, -5, 49, 5, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), 5, 44, -5, -5, 44, 5, this.floorMaterial());
	}

	@Override
	Material floorMaterial() {
		return Material.COBBLESTONE;
	}

	@Override
	Location spawnLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

}
