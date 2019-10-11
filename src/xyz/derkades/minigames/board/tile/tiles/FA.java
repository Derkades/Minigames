package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.ChooseDirectionTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class FA extends ChooseDirectionTile {

	@Override
	public XYZ getXYZ() {
		return new XYZ(159, 139, 62);
	}

	@Override
	public Tile[] getNextTiles() {
		return new Tile[] {
				new FB(),
				new FT(),
				};
	}

}
