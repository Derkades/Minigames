package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.SwapPositionsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class EA extends SwapPositionsTile {

	@Override
	public Tile getNextTile() {
		return new EB();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(144, 136, 40);
	}

}
