package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class AJ extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new AK();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(154, 134, 14);
	}

}