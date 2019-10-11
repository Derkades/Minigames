package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsGetTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FU extends CoinsGetTile {

	@Override
	public Tile getNextTile() {
		return new FV();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(130, 142, 77);
	}

}

