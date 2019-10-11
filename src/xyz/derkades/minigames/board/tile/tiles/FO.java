package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.SwapPositionsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FO extends SwapPositionsTile {

	@Override
	public Tile getNextTile() {
		return new FN();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(151, 140, 77);
	}

}
