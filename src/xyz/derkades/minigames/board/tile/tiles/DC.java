package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.SwapPositionsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class DC extends SwapPositionsTile {

	@Override
	public Tile getNextTile() {
		return new DD();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(136, 134, 26);
	}

}
