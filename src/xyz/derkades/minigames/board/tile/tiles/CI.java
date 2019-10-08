package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CI extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new CJ();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(172, 135, 60);
	}

}