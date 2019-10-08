package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class AE extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new AF();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(144, 134, 12);
	}

}
