package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class BF extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new BG();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(172, 133, 28);
	}

}