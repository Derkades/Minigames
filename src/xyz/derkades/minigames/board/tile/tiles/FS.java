package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveBackwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FS extends MoveBackwardsTile {

	@Override
	public Tile getNextTile() {
		return new FR();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(161, 139, 69);
	}

	@Override
	public Tile[] getTilesBackwards() {
		// TODO Auto-generated method stub
		return null;
	}

}
