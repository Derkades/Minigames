package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FQ extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new FP();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(157, 140, 73);
	}

}