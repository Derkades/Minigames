package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveForwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FG extends MoveForwardsTile {

	@Override
	public Tile getNextTile() {
		return new FH();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(141, 141, 76);
	}

}
