package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CA extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new CB();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(186, 134, 41);
	}

}