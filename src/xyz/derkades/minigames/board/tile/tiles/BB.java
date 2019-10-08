package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class BB extends MoveTile {

	@Override
	public MoveType getMoveType() {
		return MoveType.FORWARDS;
	}

	@Override
	public Tile getNextTile() {
		return new BC();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(161, 135, 21);
	}

}