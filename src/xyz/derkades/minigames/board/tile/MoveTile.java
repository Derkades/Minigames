package xyz.derkades.minigames.board.tile;

import java.util.function.Consumer;

import xyz.derkades.minigames.utils.MPlayer;

public abstract class MoveTile {

	public abstract MoveType getMoveType();

	public int getTilesAmount() {
		return 3;
	}

	public void moveToNextTile(final MPlayer player, final Consumer<Tile> onMove) {

	}

	public static enum MoveType {

		FORWARDS, BACKWARDS;

	}

}
