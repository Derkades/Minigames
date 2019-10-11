package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveBackwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class DD extends MoveBackwardsTile {

	@Override
	public Tile getNextTile() {
		return new DE();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(135, 135, 29);
	}

	@Override
	public Tile[] getTilesBackwards() {
		return new Tile[] {
				new DC(),
				new DB(),
				new DA(),
		};
	}

}