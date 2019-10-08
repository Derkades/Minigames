package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveForwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class BB extends MoveForwardsTile {

	@Override
	public Tile getNextTile() {
		return new BC();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(161, 135, 21);
	}

}