package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CP extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new FA();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(159, 139, 58);
	}

}