package xyz.derkades.minigames.board.tile;

import java.util.function.Consumer;

import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.minigames.utils.MPlayer;

public abstract class ChooseDirectionTile extends DynamicDirectionTile {

	@Override
	public void moveToNextTile(final MPlayer player, final Consumer<Tile> onMove) {
		// Choose a random tile for now
		// TODO Implement tile choosing
		onMove.accept(ListUtils.getRandomValueFromArray(this.getNextTiles()));
	}

	@Override
	public void landOnTile(final MPlayer player) {
		// Do nothing, like a blank tile.
		// TODO Or, move player one more tile?
	}

}
