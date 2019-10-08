package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

public abstract class MoveBackwardsTile extends MoveTile {

	private static final int AMOUNT = 3;

	@Override
	public int getTilesAmount() {
		return -AMOUNT;
	}

	@Override
	public String getName() {
		return "Move Backwards";
	}

	@Override
	public String getDescription() {
		return "Move backwards " + AMOUNT + " tiles.";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_RED;
	}

}
