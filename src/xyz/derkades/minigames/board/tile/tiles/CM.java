package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveForwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CM extends MoveForwardsTile {

	@Override
	public Tile getNextTile() {
		return new CN();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(163, 136, 53);
	}

}
