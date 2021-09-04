package derkades.minigames.games.tron;

import org.bukkit.Location;

class TronSpawnLocation {

	private final Location location;
	private final Direction direction;

	TronSpawnLocation(final Location location, final Direction direction) {
		this.location = location;
		this.direction = direction;
	}

	Location getLocation() {
		return this.location;
	}

	Direction getDirection() {
		return this.direction;
	}

}
