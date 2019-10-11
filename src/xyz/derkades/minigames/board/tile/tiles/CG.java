package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveBackwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class CG extends MoveBackwardsTile {

	@Override
	public Tile getNextTile() {
		return new CH();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(177, 134, 56);
	}

	@Override
	public Tile[] getTilesBackwards() {
		return new Tile[] {
				new CF(),
				new CE(),
				new CD(),
		};
	}

}
