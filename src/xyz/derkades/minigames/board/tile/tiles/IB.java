package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsGetTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class IB extends CoinsGetTile {

	@Override
	public Tile getNextTile() {
		return new IC();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(138, 157, 29);
	}

}


