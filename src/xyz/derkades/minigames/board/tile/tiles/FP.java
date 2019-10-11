package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsGetTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FP extends CoinsGetTile {

	@Override
	public Tile getNextTile() {
		return new FO();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(154, 140, 75);
	}

}