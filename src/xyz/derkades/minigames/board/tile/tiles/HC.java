package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsLoseTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class HC extends CoinsLoseTile {

	@Override
	public Tile getNextTile() {
		return new HD();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(120, 154, 31);
	}

}

