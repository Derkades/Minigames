package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import xyz.derkades.minigames.board.BoardPlayer;

public abstract class MoveBackwardsTile extends MoveTile {

	public abstract Tile[] getTilesBackwards();

	@Override
	public String getName() {
		return "Move Backwards";
	}

	@Override
	public String getDescription() {
		return "Move backwards " + getTilesBackwards().length + " tiles.";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_RED;
	}

	@Override
	public void landOnTile(final BoardPlayer player) {
		for (final Tile tile : getTilesBackwards()) {
			player.jumpTile(tile);
		}
	}

}
