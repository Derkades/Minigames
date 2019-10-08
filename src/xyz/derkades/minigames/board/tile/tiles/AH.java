package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveForwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class AH extends MoveForwardsTile {

	@Override
	public Tile getNextTile() {
		return new AI();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(152, 133, 7);
	}

}
