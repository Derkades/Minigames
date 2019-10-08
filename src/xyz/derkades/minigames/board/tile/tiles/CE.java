package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CE extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new CF();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(182, 134, 52);
	}

}
