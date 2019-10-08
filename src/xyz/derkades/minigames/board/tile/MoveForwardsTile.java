package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

public abstract class MoveForwardsTile extends MoveTile {

	@Override
	public int getTilesAmount() {
		return -3;
	}

	@Override
	public String getName() {
		return "Move Forwards";
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public ChatColor getColor() {
		return null;
	}

}
