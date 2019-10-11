package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class ID extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new IE();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(140, 159, 37);
	}

}
