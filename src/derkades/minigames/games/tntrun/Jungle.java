package derkades.minigames.games.tntrun;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;
import xyz.derkades.derkutils.bukkit.BlockUtils;

class Jungle extends TntRunMap {

	@Override
	public @NotNull String getName() {
		return "Jungle";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
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
	public @NotNull String getIdentifier() {
		return "tntrun_jungle";
	}

	@Override
	void restore() {
		BlockUtils.fillArea(this.getWorld(), -6, 64, 6, 6, 64, -6, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), -6, 59, 6, 6, 59, -6, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), -6, 54, 6, 6, 54, -6, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), -6, 49, 6, 6, 49, -6, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), -6, 44, 6, 6, 44, -6, this.floorMaterial());
	}

	@Override
	Material floorMaterial() {
		return Material.TNT;
	}

	@Override
	Location spawnLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

}
