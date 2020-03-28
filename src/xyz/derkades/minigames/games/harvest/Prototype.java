package xyz.derkades.minigames.games.harvest;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class Prototype extends HarvestMap {

	@Override
	public String getName() {
		return "Prototype";
	}
	
	@Override
	public List<Location> getCropLocations() {
		final int minX = 308;
		final int maxX = 326;
		final int y = 69;
		final int minZ = 238;
		final int maxZ = 256;
		final List<Location> blocks = new ArrayList<>();
		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				blocks.add(new Location(Var.WORLD, x, y, z));
			}
		}
		return blocks;
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(Var.WORLD, 317, 75, 247);
	}

	@Override
	public GameWorld getGameWorld() {
		return null;
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
		return "harvest_prototype";
	}

}
