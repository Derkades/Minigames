package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import xyz.derkades.minigames.constants.BoardConfig;

public abstract class MoveBackwardsTile extends MoveTile {

	@Override
	public int getTilesAmount() {
		return -BoardConfig.TILE_MOVE_BACKWARDS_AMOUNT;
	}

	@Override
	public String getName() {
		return "Move Backwards";
	}

	@Override
	public String getDescription() {
		return "Move backwards " + BoardConfig.TILE_MOVE_BACKWARDS_AMOUNT + " tiles.";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_RED;
	}

}
