package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CF extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new CG();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(180, 134, 55);
	}

}