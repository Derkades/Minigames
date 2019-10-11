package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveForwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class HD extends MoveForwardsTile {

	@Override
	public Tile getNextTile() {
		return new HE();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(123, 154, 29);
	}

}