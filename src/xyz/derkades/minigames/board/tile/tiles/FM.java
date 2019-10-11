package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsLoseTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FM extends CoinsLoseTile {

	@Override
	public Tile getNextTile() {
		return new FL();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(145, 141, 81);
	}

}

