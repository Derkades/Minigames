package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.board.BoardPlayer;

public abstract class CoinsGetTile extends CoinsTile {

	private static final int AMOUNT = 10;

	@Override
	public String getName() {
		return "Coins get";
	}

	@Override
	public String getDescription() {
		return "Get " + AMOUNT + " GladeCoins";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.LIGHT_PURPLE;
	}

	@Override
	public void landOnTile(final BoardPlayer player) {
		Minigames.economy.depositPlayer(player.bukkit(), AMOUNT);

	}

}
