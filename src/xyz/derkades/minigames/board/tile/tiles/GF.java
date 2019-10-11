package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsLoseTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class GF extends CoinsLoseTile {

	@Override
	public Tile getNextTile() {
		return new GG();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(111, 149, 51);
	}

}