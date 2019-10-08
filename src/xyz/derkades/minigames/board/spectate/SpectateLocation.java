package xyz.derkades.minigames.board.spectate;

import org.bukkit.Location;

import xyz.derkades.minigames.Logger;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.board.BoardPlayer;

public enum SpectateLocation {

	A(149, 140, -3),
	B(160, 141, 29),
	C(178, 143, 50),
	D(132, 140, 32),
	E(149, 139, 42),
	F(142, 146, 66),
	G(106, 153, 59),

	;

	private static final int LOCATION_TOLERANCE = 2;

	private final int x, y, z;

	SpectateLocation(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	private Location getLocation() {
		return new Location(Var.LOBBY_WORLD, this.x + 0.5, this.y, this.z + 0.5);
	}

	public void teleportIfOutside(final BoardPlayer player, final boolean queue) {
		final Location loc = player.getLocation();
		if (loc.getX() + LOCATION_TOLERANCE > this.x && loc.getX() - LOCATION_TOLERANCE < this.x
				&& loc.getY() + LOCATION_TOLERANCE > this.y && loc.getY() - LOCATION_TOLERANCE < this.y
				&& loc.getZ() + LOCATION_TOLERANCE > this.z && loc.getZ() - LOCATION_TOLERANCE < this.z) {
			Logger.debug("Not teleported '%s', in range of spectate location.", player.getName());
			return;
		}

		Logger.debug("Teleporting '%s', to spectator location %s queue %s.", player.getName(), name(), queue);

		if (queue) {
			player.queueTeleport(getLocation());
		} else {
			player.teleport(getLocation());
		}
	}

}