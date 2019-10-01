package xyz.derkades.minigames.board;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Minigames.ShutdownReason;

public enum Tile {

	ONE(1, TileType.BLANK);

	static final Tile START_TILE = Tile.ONE;
	static final Tile END_TILE = null;

	private int position;
	private TileType type;

	private Tile(final int position, final TileType type) {
		this.position = position;
		this.type = type;
	}

	public int getPosition() {
		return this.position;
	}

	public TileType getType() {
		return this.type;
	}

	public static Tile atPosition(final int position) {
		for (final Tile tile : Tile.values()) {
			if (tile.getPosition() == position)
				return tile;
		}

		Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Unknown tile position " + position);
		return null;
	}

}
