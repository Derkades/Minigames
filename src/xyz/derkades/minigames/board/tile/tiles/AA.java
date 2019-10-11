package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class AA extends BlankTile {

	@Override
	public XYZ getXYZ() {
		return new XYZ(141, 133, -1);
	}

	@Override
	public Tile getNextTile() {
		return new AB();
	}

}
