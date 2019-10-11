package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class GG extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new GH();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(110, 151, 47);
	}

}
