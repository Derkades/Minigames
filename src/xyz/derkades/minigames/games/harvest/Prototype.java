package xyz.derkades.minigames.games.harvest;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class Prototype extends HarvestMap {

	@Override
	public String getName() {
		return "Prototype";
	}
	
	@Override
	public List<Location> getCropLocations() {
		final int minX = -9;
		final int minZ = -8;
		final int maxX = 9;
		final int maxZ = 10;
		final int y = 65;
		final List<Location> blocks = new ArrayList<>();
		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				blocks.add(new Location(this.getWorld(), x, y, z));
			}
		}
		return blocks;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.HARVEST_PROTOTYPE;
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
