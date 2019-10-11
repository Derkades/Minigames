package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class GB extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new GC();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(121, 147, 64);
	}

}