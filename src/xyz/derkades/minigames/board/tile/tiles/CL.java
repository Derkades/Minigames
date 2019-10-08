package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CL extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new CM();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(164, 136, 56);
	}

}