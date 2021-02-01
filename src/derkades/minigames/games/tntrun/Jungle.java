package derkades.minigames.games.tntrun;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import xyz.derkades.derkutils.bukkit.BlockUtils;

public class Jungle extends TNTMap {

	@Override
	public void restore() {
		BlockUtils.fillArea(this.getWorld(), -6, 64, 6, 6, 64, -6, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), -6, 59, 6, 6, 59, -6, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), -6, 54, 6, 6, 54, -6, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), -6, 49, 6, 6, 49, -6, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), -6, 44, 6, 6, 44, -6, this.floorMaterial());
	}

	@Override
	public Material floorMaterial() {
		return Material.TNT;
	}

	@Override
	public Location spawnLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

	@Override
	public String getName() {
		return "Jungle";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.TNTRUN_JUNGLE;
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
		return "tntrun_jungle";
	}

}
