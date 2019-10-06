package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.Tile;
import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.utils.XYZ;

public class AB extends BlankTile {

	@Override
	public XYZ getXYZ() {
		return null;
	}

	@Override
	public Tile getNext() {
		return new AC();
	}

	@Override
	public Tile getPrevious() {
		return new AA();
	}

}
