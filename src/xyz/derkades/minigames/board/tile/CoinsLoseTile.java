package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import net.milkbowl.vault.economy.Economy;
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
		final Economy econ = Minigames.economy;
		final int amount = BoardConfig.TILE_COINS_LOSE_AMOUNT;
		if (econ.has(player.bukkit(), amount)) {
			econ.withdrawPlayer(player.bukkit(), amount);
		} else {
			econ.withdrawPlayer(player.bukkit(), econ.getBalance(player.bukkit()));
		}
	}

}
