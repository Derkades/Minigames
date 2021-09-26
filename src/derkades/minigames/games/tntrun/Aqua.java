package derkades.minigames.games.tntrun;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;
import xyz.derkades.derkutils.bukkit.BlockUtils;

class Aqua extends TntRunMap {

	@Override
	public @NotNull String getName() {
		return "Aqua";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.TNTRUN_AQUA;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return Size.NORMAL;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "tntrun_aqua";
	}

	@Override
	void restore() {
		BlockUtils.fillArea(this.getWorld(), 12, 64, -6, -12, 64, 6, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), 12, 59, -6, -12, 59, 6, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), 12, 54, -6, -12, 54, 6, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), 12, 49, -6, -12, 49, 6, this.floorMaterial());
		BlockUtils.fillArea(this.getWorld(), 12, 44, -6, -12, 44, 6, this.floorMaterial());
	}

	@Override
	Material floorMaterial() {
		return Material.PACKED_ICE;
	}

	@Override
	Location spawnLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

}
