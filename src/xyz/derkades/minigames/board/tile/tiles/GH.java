package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class GH extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new GI();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(111, 152, 44);
	}

}