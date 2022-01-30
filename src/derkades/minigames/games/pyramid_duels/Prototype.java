package derkades.minigames.games.pyramid_duels;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.SoftReference;

public class Prototype extends PyramidDuelsMap {

	@Override
	public @NotNull String getName() {
		return "Prototype";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.PYRAMIDDUELS_PROTOTYPE;
	}

	@Override
	public @Nullable String getCredits() {
		return null;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "pyramid_duels_prototype";
	}

	private SoftReference<PyramidNode> rootNodeCache = null;

	@Override
	public @NotNull PyramidNode getRootNode() {
		PyramidNode root;
		if (rootNodeCache == null) {
			root = getPyramidNode(0, 0);
			rootNodeCache = new SoftReference<>(root);
		} else {
			root = rootNodeCache.get();
			if (root == null) {
				root = getPyramidNode(0, 0);
				rootNodeCache = new SoftReference<>(root);
			}
		}
		return root;
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(this.getWorld(), 0.5, 80, -24.5);
	}

	private void gateReplacer(boolean closed, Location centerLocation) {
		Block center = centerLocation.getBlock();
		Block[] replaceBlocks = new Block[] {
				center.getRelative(-2, 0, 5),
				center.getRelative(-3, 0, 5),
				center.getRelative(-2, 1, 5),
				center.getRelative(-3, 1, 5),
				center.getRelative(2, 0, 5),
				center.getRelative(3, 0, 5),
				center.getRelative(2, 1, 5),
				center.getRelative(3, 1, 5),
		};
		for (Block replaceBlock : replaceBlocks) {
			Material current = replaceBlock.getType();
			if (closed && current == Material.AIR ||
				!closed && current == Material.OAK_PLANKS) {
				replaceBlock.setType(closed ? Material.OAK_PLANKS : Material.AIR);
			}
		}
	}

	private @NotNull PyramidNode getPyramidNode(int depthLevel, int x) {
		Location thisLocation = new Location(this.getWorld(), x, 65, -10 * depthLevel);
		final @Nullable PyramidNode[] children;
		if (depthLevel < 4) {
			children = new PyramidNode[] {
					getPyramidNode(depthLevel + 1, x + 5), // left child
					getPyramidNode(depthLevel + 1, x - 5), // right child
			};
		} else if (depthLevel == 4) {
			children = null;
		} else {
			throw new IllegalStateException(String.valueOf(depthLevel));
		}
		return new PyramidNode(thisLocation, 5, children, this::gateReplacer);
	}

	@Override
	public Size getSize() {
		return null;
	}

}
