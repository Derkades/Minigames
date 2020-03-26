package xyz.derkades.minigames.games.decay;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.worlds.GameWorld;

public class SpruceBrick extends DecayMap {

	@Override
	public Size getSize() {
		return Size.NORMAL;
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

	@Override
	public Location[] getBlocks() {
		final int r = 9;
		final int y = 64;
		final List<Location> blocks = new ArrayList<>(r*r);
		for (int x = -r; x <= r; x++) {
			for (int z = -r; z <= r; z++) {
				blocks.add(new Location(this.getWorld(), x, y, z));
			}
		}
		return blocks.toArray(new Location[] {});
	}
	
	@Override
	public boolean isDead(final MPlayer player) {
		return player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.BLACK_CONCRETE);
	}

	@Override
	public String getName() {
		return "Spruce Brick Trapdoor";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.DECAY_SPRUCEBRICK;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return "decay_sprucebrick";
	}

}
