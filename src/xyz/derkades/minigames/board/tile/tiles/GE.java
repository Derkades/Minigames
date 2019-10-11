package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class GE extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new GF();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(112, 148, 55);
	}

}