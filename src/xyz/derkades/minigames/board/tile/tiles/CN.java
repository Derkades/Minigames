package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.CoinsGetTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CN extends CoinsGetTile {

	@Override
	public Tile getNextTile() {
		return new CO();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(159, 137, 51);
	}

}