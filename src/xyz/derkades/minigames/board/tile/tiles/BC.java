package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class BC extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new BD();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(164, 134, 22);
	}

}