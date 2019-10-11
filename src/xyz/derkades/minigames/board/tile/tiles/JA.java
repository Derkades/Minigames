package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class JA extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new JB();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(135, 163, 50);
	}

}
