package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsLoseTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class AF extends CoinsLoseTile {

	@Override
	public Tile getNextTile() {
		return new AG();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(143, 133, 17);
	}

}
