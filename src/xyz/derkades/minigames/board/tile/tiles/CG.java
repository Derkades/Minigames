package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CG extends MoveTile {

	@Override
	public Tile getNextTile() {
		return new CH();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(177, 134, 56);
	}

	@Override
	public MoveType getMoveType() {
		return MoveType.BACKWARDS;
	}

}
