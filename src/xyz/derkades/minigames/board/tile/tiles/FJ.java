package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FJ extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new FU();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(133, 141, 79);
	}

}