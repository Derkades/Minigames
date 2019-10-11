package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class EC extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new ED();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(149, 136, 46);
	}

}