package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsLoseTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class DB extends CoinsLoseTile {

	@Override
	public Tile getNextTile() {
		return new DC();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(138, 134, 23);
	}

}
