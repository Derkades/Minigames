package derkades.minigames.games.tron;

import org.bukkit.Location;

record TronSpawnLocation(Location location, Direction direction) {

	@Deprecated
	Location getLocation() {
		return this.location;
	}

	@Deprecated
	Direction getDirection() {
		return this.direction;
	}

}
