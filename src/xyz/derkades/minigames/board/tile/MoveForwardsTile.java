package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import xyz.derkades.minigames.constants.BoardConfig;

public abstract class MoveForwardsTile extends MoveTile {

	@Override
	public int getTilesAmount() {
		return BoardConfig.TILE_MOVE_FORWARDS_AMOUNT;
	}

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

}
