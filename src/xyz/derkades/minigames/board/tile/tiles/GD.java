package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class GD extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new GE();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(115, 148, 58);
	}

}