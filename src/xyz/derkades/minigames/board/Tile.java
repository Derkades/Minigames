package xyz.derkades.minigames.board;

public abstract class Tile {

//	static final Tile START_TILE = Tile.ONE;
//	static final Tile END_TILE = null;

	public abstract int getPosition();

//	public static Tile atPosition(final int position) {
//		for (final Tile tile : Tile.values()) {
//			if (tile.getPosition() == position)
//				return tile;
//		}
//
//		Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Unknown tile position " + position);
//		return null;
//	}

}
