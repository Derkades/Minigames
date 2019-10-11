package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FC extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new FD();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(152, 140, 64);
	}

}