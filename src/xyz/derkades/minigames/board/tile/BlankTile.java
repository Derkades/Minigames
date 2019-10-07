package xyz.derkades.minigames.board.tile;

import xyz.derkades.minigames.board.BoardPlayer;

public abstract class BlankTile extends StaticDirectionTile {

	@Override
	public void landOnTile(final BoardPlayer player) {
		// Do nothing, it's a blank tile
	}

}