package xyz.derkades.minigames.board.tile;

import java.util.function.Consumer;

import xyz.derkades.minigames.board.BoardPlayer;
import xyz.derkades.minigames.utils.Scheduler;

public abstract class StaticDirectionTile extends Tile {

	public abstract Tile getNextTile();

	@Override
	public void moveToNextTile(final BoardPlayer player, final Consumer<Tile> onMove) {
		Scheduler.delay(Tile.TILE_TELEPORT_DELAY, () -> onMove.accept(this.getNextTile()));
	}

}
