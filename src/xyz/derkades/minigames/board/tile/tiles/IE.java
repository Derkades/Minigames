package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsLoseTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class IE extends CoinsLoseTile {

	@Override
	public Tile getNextTile() {
		return new IF();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(140, 160, 41);
	}

}