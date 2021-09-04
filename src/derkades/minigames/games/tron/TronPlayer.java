package derkades.minigames.games.tron;

import org.bukkit.scheduler.BukkitTask;

import derkades.minigames.Minigames;
import derkades.minigames.Minigames.ShutdownReason;
import derkades.minigames.games.GameTeam;

class TronPlayer {

	private final GameTeam team;
	private Direction direction;
	BukkitTask blockPlacerTask;

	TronPlayer(final GameTeam team, final TronSpawnLocation spawnLocation) {
		this.team = team;
		this.direction = spawnLocation.getDirection();
	}

	GameTeam getTeam() {
		return this.team;
	}

	Direction getDirection() {
		return this.direction;
	}

	void rotateLeft() {
		switch(this.direction) {
		case NORTH:
			this.direction = Direction.WEST;
			break;
		case WEST:
			this.direction = Direction.SOUTH;
			break;
		case SOUTH:
			this.direction = Direction.EAST;
			break;
		case EAST:
			this.direction = Direction.NORTH;
			break;
		default:
			Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Illegal direction '" + this.direction + "'");
		}
	}

	void rotateRight() {
		switch(this.direction) {
		case NORTH:
			this.direction = Direction.EAST;
			break;
		case EAST:
			this.direction = Direction.SOUTH;
			break;
		case SOUTH:
			this.direction = Direction.WEST;
			break;
		case WEST:
			this.direction = Direction.NORTH;
			break;
		default:
			Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Illegal direction '" + this.direction + "'");
		}
	}

}