package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import xyz.derkades.minigames.board.BoardPlayer;

public abstract class MoveOtherPlayerTile extends StaticDirectionTile {

	@Override
	public void landOnTile(final BoardPlayer player) {
		// TODO open gui to move other player
	}

	@Override
	public String getName() {
		return "Move player";
	}

	@Override
	public String getDescription() {
		return "Move another player forwards or backwards a number of steps.";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.AQUA;
	}


}
