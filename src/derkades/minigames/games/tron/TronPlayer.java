package derkades.minigames.games.tron;

import derkades.minigames.games.GameTeam;
import org.bukkit.scheduler.BukkitTask;

class TronPlayer {

	private final GameTeam team;
	private Direction direction;
	BukkitTask blockPlacerTask;

	TronPlayer(final GameTeam team, final TronSpawnLocation spawnLocation) {
		this.team = team;
		this.direction = spawnLocation.direction();
	}

	GameTeam getTeam() {
		return this.team;
	}

	Direction getDirection() {
		return this.direction;
	}

	void rotateLeft() {
		this.direction = switch (this.direction) {
			case NORTH -> Direction.WEST;
			case WEST -> Direction.SOUTH;
			case SOUTH -> Direction.EAST;
			case EAST -> Direction.NORTH;
		};
	}

	void rotateRight() {
		this.direction = switch (this.direction) {
			case NORTH -> Direction.EAST;
			case EAST -> Direction.SOUTH;
			case SOUTH -> Direction.WEST;
			case WEST -> Direction.NORTH;
		};
	}

}