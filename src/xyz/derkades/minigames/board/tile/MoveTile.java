package xyz.derkades.minigames.board.tile;

import xyz.derkades.minigames.board.BoardPlayer;

public abstract class MoveTile extends StaticDirectionTile {

	public abstract int getTilesAmount();

	@Override
	public void landOnTile(final BoardPlayer player) {
		player.jumpTiles(getTilesAmount());
	}

}
