package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class IA extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new IB();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(135, 156, 27);
	}

}