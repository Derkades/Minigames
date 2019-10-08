package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import xyz.derkades.minigames.board.BoardPlayer;

public abstract class SwapPositionsTile extends StaticDirectionTile {

	@Override
	public String getName() {
		return "Swap";
	}

	@Override
	public String getDescription() {
		return "Swap positions with another player";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_GRAY;
	}

	@Override
	public void landOnTile(final BoardPlayer player) {
		player.sendChat("swap tile not implemented yet");
		// TODO open swap gui
	}

}
