package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.SwapPositionsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CD extends SwapPositionsTile {

	@Override
	public Tile getNextTile() {
		return new CE();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(185, 134, 50);
	}

}
