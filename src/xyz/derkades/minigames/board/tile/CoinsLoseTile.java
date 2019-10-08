package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.board.BoardPlayer;
import xyz.derkades.minigames.constants.BoardConfig;

public abstract class CoinsLoseTile extends CoinsTile {

	@Override
	public String getName() {
		return "Coins lose";
	}

	@Override
	public String getDescription() {
		return "Lose " + BoardConfig.TILE_COINS_LOSE_AMOUNT + " GladeCoins";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.RED;
	}

	@Override
	public void landOnTile(final BoardPlayer player) {
		Minigames.economy.withdrawPlayer(player.bukkit(), BoardConfig.TILE_COINS_LOSE_AMOUNT);

	}

}
