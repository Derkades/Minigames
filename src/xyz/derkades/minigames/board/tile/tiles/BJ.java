package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveBackwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class BJ extends MoveBackwardsTile {

	@Override
	public Tile getNextTile() {
		return new CA();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(183, 134, 39);
	}

}