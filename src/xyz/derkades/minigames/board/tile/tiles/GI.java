package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class GI extends BlankTile {

	@Override
	public Tile getNextTile() {
		return new HA();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(112, 152, 40);
	}

}