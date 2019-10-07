package xyz.derkades.minigames.board.tile;

import xyz.derkades.minigames.board.BoardPlayer;

public abstract class MoveTile extends StaticDirectionTile {

	public abstract MoveType getMoveType();

	public int getTilesAmount() {
		return 3;
	}

	@Override
	public void landOnTile(final BoardPlayer player) {
		player.jumpTiles(this.getTilesAmount());
	}

	public static enum MoveType {

		FORWARDS, BACKWARDS;

	}

}
