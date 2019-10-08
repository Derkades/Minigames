package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsGetTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class AK extends CoinsGetTile {

	@Override
	public Tile getNextTile() {
		return new BA();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(155, 135, 17);
	}

}
