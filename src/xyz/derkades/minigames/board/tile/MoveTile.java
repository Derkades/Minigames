package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import xyz.derkades.minigames.board.BoardPlayer;

public abstract class MoveTile extends StaticDirectionTile {

	public abstract MoveType getMoveType();

	public int getTilesAmount() {
		return 3;
	}

	@Override
	public void landOnTile(final BoardPlayer player) {
		player.jumpTiles(getTilesAmount());
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getSubtitle() {
		return null;
	}

	@Override
	public ChatColor getColor() {
		return null;
	}


	public enum MoveType {

		FORWARDS, BACKWARDS;

	}

}
