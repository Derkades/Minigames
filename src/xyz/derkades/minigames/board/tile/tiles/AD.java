package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class AD extends MoveTile {

	@Override
	public Tile getNextTile() {
		return new AE();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(146, 133, 8);
	}

	@Override
	public MoveType getMoveType() {
		return MoveType.BACKWARDS;
	}

}
