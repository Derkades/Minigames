package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class BH extends MoveTile {

	@Override
	public Tile getNextTile() {
		return new BI();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(176, 134, 34);
	}

	@Override
	public MoveType getMoveType() {
		return MoveType.BACKWARDS;
	}

}