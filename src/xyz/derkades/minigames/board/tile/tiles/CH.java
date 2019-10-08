package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CH extends CoinsTile {

	@Override
	public Tile getNextTile() {
		return new CI();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(175, 134, 59);
	}

	@Override
	public CoinsType getType() {
		return CoinsType.TAKE;
	}

}