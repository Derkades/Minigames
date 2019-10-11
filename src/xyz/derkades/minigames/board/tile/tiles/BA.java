package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class BA extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new BB();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(158, 135, 19);
	}

}