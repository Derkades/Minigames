package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class AG extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new AH();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(150, 133, 4);
	}

}
