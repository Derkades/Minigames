package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveBackwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FI extends MoveBackwardsTile {

	@Override
	public Tile getNextTile() {
		return new FJ();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(136, 141, 81);
	}

	@Override
	public Tile[] getTilesBackwards() {
		return new Tile[] {
				new FK(),
				new FL(),
				new FM(),
		};
	}

}