package derkades.minigames.games.freefall;

import org.bukkit.Material;
import org.bukkit.World;

import derkades.minigames.utils.MPlayer;
import xyz.derkades.derkutils.bukkit.BlockUtils;

record Hole(World world, int x1, int x2, int z1, int z2, Material material) {

	void fill(final int y, final Material material) {
		BlockUtils.fillArea(this.world, this.x1, y, this.z1, this.x2, y, this.z2, material);
	}

	void fill(final int y) {
		this.fill(y, this.material);
	}

	void empty(final int y) {
		this.fill(y, Material.AIR);
	}

	boolean isInHole(final MPlayer player) {
		return player.isIn2dBounds(this.world, this.x1, this.z1, this.x2, this.z2);
	}

}
