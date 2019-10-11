package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class HF extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new IA();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(131, 156, 26);
	}

}