package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsGetTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CB extends CoinsGetTile {

	@Override
	public Tile getNextTile() {
		return new CC();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(187, 134, 44);
	}

}