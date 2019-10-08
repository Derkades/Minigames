package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

public abstract class MoveForwardsTile extends MoveTile {

	private static final int AMOUNT = 3;

	@Override
	public int getTilesAmount() {
		return AMOUNT;
	}

	@Override
	public String getName() {
		return "Move Forwards";
	}

	@Override
	public String getDescription() {
		return "Move forwards " + AMOUNT + " tiles.";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.GREEN;
	}

}
