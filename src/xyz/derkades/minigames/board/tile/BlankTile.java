package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import xyz.derkades.minigames.board.BoardPlayer;

public abstract class BlankTile extends StaticDirectionTile {

	@Override
	public void landOnTile(final BoardPlayer player) {
		// Do nothing, it's a blank tile
		player.sendChat("test: blank tile"); // XXX remove debug message
	}

	@Override
	public String getName() {
		return "Blank";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.GRAY;
	}

}