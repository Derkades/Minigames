package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import xyz.derkades.minigames.board.BoardPlayer;
import xyz.derkades.minigames.constants.BoardConfig;

public abstract class MoveForwardsTile extends MoveTile {

	@Override
	public String getName() {
		return "Move Forwards";
	}

	@Override
	public String getDescription() {
		return "Move forwards " + BoardConfig.TILE_MOVE_FORWARDS_AMOUNT + " tiles.";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.GREEN;
	}

	@Override
	public void landOnTile(final BoardPlayer player) {
		player.jumpTiles(BoardConfig.TILE_MOVE_FORWARDS_AMOUNT);
	}

}
