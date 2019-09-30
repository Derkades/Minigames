package xyz.derkades.minigames.games.freefall;

import org.bukkit.Material;

import xyz.derkades.derkutils.ListUtils;

public class Layer {

	private static final int LIQUID_HEIGHT_RELATIVE = -3;

	private final int height;
	private final Hole[] holes;
	private Hole correctHole;

	public Layer(final int height, final Hole[] holes) {
		this.height = height;
		this.holes = holes;
	}

	public Hole setRandomCorrectHole() {
		return this.correctHole = ListUtils.getRandomValueFromArray(this.holes);
	}

	public void placeBlocks() {
		for (final Hole hole : this.holes) {
			hole.fill(this.height);
		}
	}

	public void removeBlocks() {
		for (final Hole hole : this.holes) {
			hole.empty(this.height);
		}
	}

	public void placeFluid() {
		for (final Hole hole : this.holes) {
			if (hole == this.correctHole) {
				hole.fill(this.height + LIQUID_HEIGHT_RELATIVE, Material.WATER);
			} else {
				hole.fill(this.height + LIQUID_HEIGHT_RELATIVE, Material.LAVA);
			}
		}
	}

	public void removeFluid() {
		for (final Hole hole : this.holes) {
			hole.fill(this.height + LIQUID_HEIGHT_RELATIVE, Material.AIR);
		}
	}

}
