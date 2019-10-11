import xyz.derkades.minigames.board.BoardPlayer;
import xyz.derkades.minigames.board.tile.SwapPositionsTile;
import xyz.derkades.minigames.board.tile.Tile;
import xyz.derkades.minigames.utils.XYZ;

public class IG extends SwapPositionsTile {

	@Override
	public Tile getNextTile() {
		return new JA();
	}

	@Override
	public XYZ getXYZ() {
		return new XYZ(138, 162, 47);
	}

	@Override
	public void landOnTile(BoardPlayer player) {
		// TODO Auto-generated method stub
		
	}

}