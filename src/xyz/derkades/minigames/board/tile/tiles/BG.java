package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class BG extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new BH();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(175, 133, 30);
	}

}


