package derkades.minigames.games.tron;

import org.bukkit.Location;

import derkades.minigames.games.Tron.Direction;

public class TronSpawnLocation {

	private final Location location;
	private final Direction direction;

	TronSpawnLocation(final Location location, final Direction direction) {
		this.location = location;
		this.direction = direction;
	}

	public Location getLocation() {
		return this.location;
	}

	public Direction getDirection() {
		return this.direction;
	}

}
