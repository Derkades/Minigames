package xyz.derkades.minigames.board.spectate;

import org.bukkit.Location;

import xyz.derkades.minigames.Logger;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.board.BoardPlayer;

public enum SpectateLocation {

	A(149, 140, -3, 30, 35),
	B(160, 141, 29, -130, 30),
	C(178, 139, 50, -64, 40),
	D(132, 140, 32, -97, 34),
	E(149, 139, 42, 40, 40),
	F(142, 146, 66, -35, 19),
	G(106, 153, 59, -120, 20),
	H(115, 156, 24, -40, 11),
	I(144, 161, 32, 66, 19),
	J(129, 167, 53, -162, 32),

	;

	private static final int LOCATION_TOLERANCE = 2;

	private final int x, y, z, pitch, yaw;

	SpectateLocation(final int x, final int y, final int z, final int pitch, final int yaw) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	private Location getLocation() {
		return new Location(Var.LOBBY_WORLD, this.x + 0.5, this.y, this.z + 0.5, this.pitch, this.yaw);
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