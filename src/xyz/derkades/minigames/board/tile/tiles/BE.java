package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class BE extends CoinsTile {

	@Override
	public Tile getNextTile() {
		return new BF();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(170, 133, 25);
	}

	@Override
	public CoinsType getType() {
		return CoinsType.TAKE;
	}

}
