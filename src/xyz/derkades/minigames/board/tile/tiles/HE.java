package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class HE extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new HF();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(127, 156, 27);
	}

}