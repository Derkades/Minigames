package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class DE extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new DF();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(136, 136, 32);
	}

}