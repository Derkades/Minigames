package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CM extends MoveTile {

	@Override
	public MoveType getMoveType() {
		return MoveType.FORWARDS;
	}

	@Override
	public Tile getNextTile() {
		return new CN();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(163, 136, 53);
	}

}
