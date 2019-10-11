package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FW extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new GA();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(126, 145, 71);
	}

}