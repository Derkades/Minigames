package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveBackwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class AD extends MoveBackwardsTile {

	@Override
	public Tile getNextTile() {
		return new AE();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(146, 133, 8);
	}

}
