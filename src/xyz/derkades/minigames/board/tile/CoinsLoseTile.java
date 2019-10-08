package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.board.BoardPlayer;

public abstract class CoinsLoseTile extends CoinsTile {

	private static final int AMOUNT = 10;

	@Override
	public String getName() {
		return "Coins lose";
	}

	@Override
	public String getDescription() {
		return "Lose " + AMOUNT + " GladeCoins";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.RED;
	}

	@Override
	public void landOnTile(final BoardPlayer player) {
		Minigames.economy.withdrawPlayer(player.bukkit(), AMOUNT);

	}

}
