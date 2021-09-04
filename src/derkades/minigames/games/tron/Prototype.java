package derkades.minigames.games.tron;

import org.bukkit.Location;

import derkades.minigames.games.Tron.Direction;
import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

public class Prototype extends TronMap {

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public Location getOuterCornerOne() {
		return new Location(this.getWorld(), 30, 65, 40);
	}

	@Override
	public Location getOuterCornerTwo() {
		return new Location(this.getWorld(), -30, 65, -40);
	}

	@Override
	public Location getInnerCornerOne() {
		return new Location(this.getWorld(), 29, 64, 39);
	}

	@Override
	public Location getInnerCornerTwo() {
		return new Location(this.getWorld(), -29, 64, -39);
	}

	@Override
	public TronSpawnLocation[] getSpawnLocations() {
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
//		final EnumMap<GameTeam, Location> map = new EnumMap<>(GameTeam.class);
//		map.put(GameTeam.LIGHT_BLUE, new Location(this.getWorld(), -28.5, 65, -38.5));	// 1
//		map.put(GameTeam.LIME, new Location(this.getWorld(), 28.5, 65, -38.5));			// 2
//		map.put(GameTeam.ORANGE, new Location(this.getWorld(), -28.5, 65, 38.5));		// 3
//		map.put(GameTeam.RED, new Location(this.getWorld(), 28.5, 65, 38.5));			// 4
//		map.put(GameTeam.PURPLE, new Location(this.getWorld(), 28.5, 65, 0.5));			// 5
//		map.put(GameTeam.BLUE, new Location(this.getWorld(), -28.5, 65, 0.5));			// 6
//		map.put(GameTeam.PINK, new Location(this.getWorld(), 14.5, 65, 20.5));			// 7
//		map.put(GameTeam.YELLOW, new Location(this.getWorld(), -14.5, 65, 20.5));		// 8
//		map.put(GameTeam.GREEN, new Location(this.getWorld(), 14.5, 65, -20.5));		// 9
//		map.put(GameTeam.WHITE, new Location(this.getWorld(), -14.5, 65, -20.5));		// 10
//		return map;
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(this.getWorld(), 0.5, 90, 0.5, 90, 90);
	}

//	@Override
//	public Direction getSpawnDirection(final GameTeam team) {
//		switch(team) {
//		case LIGHT_BLUE:
//		case ORANGE:
//		case BLUE:
//		case YELLOW:
//		case WHITE:
//			return Direction.EAST;
//		case LIME:
//		case RED:
//		case PURPLE:
//		case PINK:
//		case GREEN:
//			return Direction.WEST;
//		default:
//			Logger.warning("Unknown team in getSpawnDirection tron map prototype");
//			return Direction.SOUTH;
//		}
//	}

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



}
