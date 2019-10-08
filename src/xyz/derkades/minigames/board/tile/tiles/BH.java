package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveBackwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class BH extends MoveBackwardsTile {

	@Override
	public Tile getNextTile() {
		return new BI();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(176, 134, 34);
	}

}