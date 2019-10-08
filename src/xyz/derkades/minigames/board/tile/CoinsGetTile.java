package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.board.BoardPlayer;

public abstract class CoinsGetTile extends CoinsTile {

	private static final int AMOUNT = 10;

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public ChatColor getColor() {
		return null;
	}

	@Override
	public void landOnTile(final BoardPlayer player) {
		Minigames.economy.depositPlayer(player.bukkit(), AMOUNT);

	}

}
