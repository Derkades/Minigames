package derkades.minigames.games.freefall;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;

public class Layer {

	private static final int LIQUID_HEIGHT_RELATIVE = -3;

	private final int height;
	private final Hole[] holes;
	private Hole correctHole;

	Layer(final int height, final Hole[] holes) {
		this.height = height;
		this.holes = holes;
	}

	Hole setRandomCorrectHole() {
		return this.correctHole = this.holes[ThreadLocalRandom.current().nextInt(this.holes.length)];
	}

	void placeBlocks() {
		for (final Hole hole : this.holes) {
			hole.fill(this.height);
		}
	}

	void removeBlocks() {
		for (final Hole hole : this.holes) {
			hole.empty(this.height);
		}
	}

	void placeFluid() {
		for (final Hole hole : this.holes) {
			if (hole == this.correctHole) {
				hole.fill(this.height + LIQUID_HEIGHT_RELATIVE, Material.WATER);
			} else {
				hole.fill(this.height + LIQUID_HEIGHT_RELATIVE, Material.LAVA);
			}
		}
	}

	void removeFluid() {
		for (final Hole hole : this.holes) {
			hole.fill(this.height + LIQUID_HEIGHT_RELATIVE, Material.AIR);
		}
	}

}
