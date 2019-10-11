package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class JB extends BlankTile {

	@Override
	public Tile getNextTile() {
		return null;
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(131, 164, 50);
	}

}
