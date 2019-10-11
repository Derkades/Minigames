package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FR extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new FQ();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(160, 139, 72);
	}

}