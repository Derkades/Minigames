package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveForwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class GC extends MoveForwardsTile {

	@Override
	public Tile getNextTile() {
		return new GD();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(118, 147, 61);
	}

}