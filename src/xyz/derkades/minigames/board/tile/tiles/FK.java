package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveForwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FK extends MoveForwardsTile {

	@Override
	public Tile getNextTile() {
		return new FI();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(139, 141, 84);
	}

}

