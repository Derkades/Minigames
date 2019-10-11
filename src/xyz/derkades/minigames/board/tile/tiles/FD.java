package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsLoseTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FD extends CoinsLoseTile {

	@Override
	public Tile getNextTile() {
		return new FE();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(149, 141, 67);
	}

}