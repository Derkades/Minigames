package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class IC extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new ID();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(140, 158, 33);
	}

}