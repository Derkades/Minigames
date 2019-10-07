package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class AB extends BlankTile {

	@Override
	public XYZ getXYZ() {
		return null;
	}

	@Override
	public Tile getNextTile() {
		return new AC();
	}

}
