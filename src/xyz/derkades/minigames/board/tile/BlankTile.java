package xyz.derkades.minigames.board.tile;

import xyz.derkades.minigames.utils.MPlayer;

public abstract class BlankTile extends StaticDirectionTile {

	@Override
	public void landOnTile(final MPlayer player) {
		// Do nothing, it's a blank tile
	}

}