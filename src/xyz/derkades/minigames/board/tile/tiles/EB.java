package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveBackwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class EB extends MoveBackwardsTile {

	@Override
	public Tile getNextTile() {
		return new EC();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(147, 136, 43);
	}

}