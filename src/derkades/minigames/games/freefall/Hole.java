package derkades.minigames.games.freefall;

import org.bukkit.Material;
import org.bukkit.World;

import derkades.minigames.utils.MPlayer;
import xyz.derkades.derkutils.bukkit.BlockUtils;

public class Hole {

	private final World world;
	private final int x1;
	private final int z1;
	private final int x2;
	private final int z2;
	private final Material material;

	Hole(final World world, final int x1, final int x2, final int z1, final int z2, final Material material) {
		this.world = world;
		this.x1 = x1;
		this.z1 = z1;
		this.x2 = x2;
		this.z2 = z2;
		this.material = material;
	}

	void fill(final int y, final Material material) {
		BlockUtils.fillArea(this.world, this.x1, y, this.z1, this.x2, y, this.z2, material);
	}

	public void fill(final int y) {
		this.fill(y, this.material);
	}

	public void empty(final int y) {
		this.fill(y, Material.AIR);
	}

	public boolean isInHole(final MPlayer player) {
		return player.isIn2dBounds(this.world, this.x1, this.z1, this.x2, this.z2);
	}

}
