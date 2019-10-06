package xyz.derkades.minigames.board.tile;

public abstract class MoveTile {

	public abstract MoveType getMoveType();

	public int getTilesAmount() {
		return 3;
	}

	public static enum MoveType {

		FORWARDS, BACKWARDS;

	}

}
