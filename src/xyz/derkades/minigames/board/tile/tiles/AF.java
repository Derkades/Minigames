package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class AF extends CoinsTile {

	@Override
	public Tile getNextTile() {
		return new AG();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(143, 133, 17);
	}

	@Override
	public CoinsType getType() {
		return CoinsType.TAKE;
	}

}
