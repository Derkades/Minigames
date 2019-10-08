package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class AH extends MoveTile {

	@Override
	public MoveType getMoveType() {
		return MoveType.FORWARDS;
	}

	@Override
	public Tile getNextTile() {
		return new AI();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(152, 133, 7);
	}

}
