package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CN extends CoinsTile {

	@Override
	public CoinsType getType() {
		return CoinsType.GET;
	}

	@Override
	public Tile getNextTile() {
		return new CO();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(159, 137, 51);
	}

}
