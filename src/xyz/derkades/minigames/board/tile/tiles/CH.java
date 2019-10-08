package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsLoseTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CH extends CoinsLoseTile {

	@Override
	public Tile getNextTile() {
		return new CI();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(175, 134, 59);
	}

}