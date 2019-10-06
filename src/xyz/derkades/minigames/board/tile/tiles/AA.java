package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.Tile;
import xyz.derkades.minigames.board.tile.BlankTile;
import xyz.derkades.minigames.utils.XYZ;

public class AA extends BlankTile {

	@Override
	public XYZ getXYZ() {
		return new XYZ(141, 133, -1);
	}

	@Override
	public Tile getNext() {
		return new AB();
	}

	@Override
	public Tile getPrevious() {
		return null;
	}

}
