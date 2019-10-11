package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FV extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new FW();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(127, 143, 74);
	}

}