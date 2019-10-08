package xyz.derkades.minigames.board.tile.tiles;

import xyz.derkades.minigames.board.tile.ChooseDirectionTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class AC extends ChooseDirectionTile {

	@Override
	public XYZ getXYZ() {
		return new XYZ(146, 133, 4);
	}

	@Override
	public Tile[] getNextTiles() {
		return new Tile[] {
				new AD(),
				new AG(),
				};
	}

}
