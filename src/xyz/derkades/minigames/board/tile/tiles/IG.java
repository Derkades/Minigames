package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.SwapPositionsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class IG extends SwapPositionsTile {

	@Override
	public Tile getNextTile() {
		return new JA();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(138, 162, 47);
	}

}