package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class BI extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new BJ();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(179, 134, 37);
	}

}