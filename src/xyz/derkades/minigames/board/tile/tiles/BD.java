package xyz.derkades.minigames.board.tile.tiles;

import java.util.function.Consumer;

import xyz.derkades.minigames.board.BoardPlayer;
import xyz.derkades.minigames.board.tile.MoveOtherPlayerTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class BD extends MoveOtherPlayerTile {

	@Override
	public XYZ getXYZ() {
		return new XYZ(167, 133, 23);
	}

	@Override
	public void moveToNextTile(BoardPlayer player, Consumer<Tile> onMove) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void landOnTile(BoardPlayer player) {
		// TODO Auto-generated method stub
		
	}

}
