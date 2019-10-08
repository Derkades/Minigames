package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import xyz.derkades.minigames.board.BoardPlayer;

public abstract class BlankTile extends StaticDirectionTile {

	@Override
	public void landOnTile(final BoardPlayer player) {
		// Do nothing, it's a blank tile
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

}