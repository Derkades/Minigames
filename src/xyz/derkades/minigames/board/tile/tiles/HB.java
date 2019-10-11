package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.MoveBackwardsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class HB extends MoveBackwardsTile {

	@Override
	public Tile getNextTile() {
		return new HC();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(117, 153, 33);
	}

	@Override
	public Tile[] getTilesBackwards() {
		return new Tile[] {
				new HA(),
				new GI(),
				new GH(),
		};
	}

}