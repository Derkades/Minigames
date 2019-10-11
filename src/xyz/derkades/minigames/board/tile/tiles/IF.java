package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class IF extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new IG();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(139, 161, 44);
	}

}
