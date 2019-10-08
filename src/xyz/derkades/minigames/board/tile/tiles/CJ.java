package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveOtherPlayerTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CJ extends MoveOtherPlayerTile {

	@Override
	public XYZ getXYZ() {
		return new XYZ(169, 136, 62);
	}

	@Override
	public Tile getNextTile() {
		return new CK();
	}

}
