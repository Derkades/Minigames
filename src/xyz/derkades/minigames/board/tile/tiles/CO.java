package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CO extends CoinsTile {

	@Override
	public Tile getNextTile() {
		return new CP();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(159, 138, 54);
	}

	@Override
	public CoinsType getType() {
		return CoinsType.TAKE;
	}

}