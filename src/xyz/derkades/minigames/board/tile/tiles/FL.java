package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FL extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new FK();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(142, 141, 83);
	}

}