package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class DF extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new DG();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(138, 137, 35);
	}

}