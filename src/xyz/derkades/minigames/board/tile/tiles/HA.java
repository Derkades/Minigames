package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class HA extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new HB();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(114, 152, 36);
	}

}