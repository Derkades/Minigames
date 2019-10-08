package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CK extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new CL();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(166, 136, 60);
	}

}