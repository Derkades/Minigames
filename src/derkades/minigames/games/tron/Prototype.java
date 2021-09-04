package derkades.minigames.games.tron;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

class Prototype extends TronMap {

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.TRON_PROTOTYPE;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return "tron_prototype";
	}

	@Override
	Location getOuterCornerOne() {
		return new Location(this.getWorld(), 30, 65, 40);
	}

	@Override
	Location getOuterCornerTwo() {
		return new Location(this.getWorld(), -30, 65, -40);
	}

	@Override
	Location getInnerCornerOne() {
		return new Location(this.getWorld(), 29, 64, 39);
	}

	@Override
	Location getInnerCornerTwo() {
		return new Location(this.getWorld(), -29, 64, -39);
	}

	@Override
	TronSpawnLocation[] getSpawnLocations() {
		return new TronSpawnLocation[] {
				new TronSpawnLocation(new Location(this.getWorld(), -28.5, 65, -38.5), Direction.EAST),
				new TronSpawnLocation(new Location(this.getWorld(), 28.5, 65, -38.5), Direction.WEST),
				new TronSpawnLocation(new Location(this.getWorld(), -28.5, 65, 38.5), Direction.EAST),
				new TronSpawnLocation(new Location(this.getWorld(), 28.5, 65, 38.5), Direction.WEST),
				new TronSpawnLocation(new Location(this.getWorld(), 28.5, 65, 0.5), Direction.EAST),
				new TronSpawnLocation(new Location(this.getWorld(), -28.5, 65, 0.5), Direction.WEST),
				new TronSpawnLocation(new Location(this.getWorld(), 14.5, 65, 20.5), Direction.EAST),
				new TronSpawnLocation(new Location(this.getWorld(), -14.5, 65, 20.5), Direction.WEST),
				new TronSpawnLocation(new Location(this.getWorld(), 14.5, 65, -20.5), Direction.EAST),
				new TronSpawnLocation(new Location(this.getWorld(), -14.5, 65, -20.5), Direction.WEST),
		};
	}

	@Override
	Location getSpectatorLocation() {
		return new Location(this.getWorld(), 0.5, 90, 0.5, 90, 90);
	}

}
