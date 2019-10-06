package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.Tile;
import xyz.derkades.minigames.board.tile.ChooseTile;
import xyz.derkades.minigames.utils.XYZ;

public class AC extends ChooseTile {

	@Override
	public XYZ getXYZ() {
		return null;
	}

	@Override
	public Tile getPrevious() {
		return new AB();
	}

}
