package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class DA extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new DB();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(141, 133, 20);
	}

}