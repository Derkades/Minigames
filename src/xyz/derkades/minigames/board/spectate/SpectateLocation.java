package xyz.derkades.minigames.board.spectate;

import org.bukkit.Location;

import xyz.derkades.minigames.Logger;
import xyz.derkades.minigames.utils.MPlayer;

public enum SpectateLocation {

	A(149, 140, -3),
	B(160, 141, 29),
	C(178, 143, 50),

	;

	private static final int LOCATION_TOLERANCE = 2;

	private final int x, y, z;

	SpectateLocation(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void teleportIfOutside(final MPlayer player) {
		final Location loc = player.getLocation();
		if (loc.getX() + LOCATION_TOLERANCE < this.x && loc.getX() - LOCATION_TOLERANCE > this.x
				&& loc.getY() + LOCATION_TOLERANCE < this.y && loc.getY() - LOCATION_TOLERANCE > this.y
				&& loc.getZ() + LOCATION_TOLERANCE < this.z && loc.getZ() - LOCATION_TOLERANCE > this.z) {
			Logger.debug("Not teleported '%s', in range of spectate location.", player.getName());
		}
	}

}