package xyz.derkades.minigames.board;

import org.bukkit.Location;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Minigames.ShutdownReason;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.XYZ;

public abstract class Tile {

	static final Tile START_TILE = null;
	static final Tile END_TILE = null;

	public abstract XYZ getXYZ();

	public abstract Tile getPrevious();

	public abstract Tile getNext();

	public Location getLocation() {
		return this.getXYZ().getLocation(Var.LOBBY_WORLD);
	}

	public static Tile atPosition(final int position) {
		for (final Tile tile : Tile.TILES) {
			if (tile.getPosition() == position)
				return tile;
		}

		Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Unknown tile position " + position);
		return null;
	}

}
