package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveOtherPlayerTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class GA extends MoveOtherPlayerTile {

	@Override
	public XYZ getXYZ() {
		return new XYZ(123, 146, 68);
	}

	@Override
	public Tile getNextTile() {
		return new GB();
	}

}