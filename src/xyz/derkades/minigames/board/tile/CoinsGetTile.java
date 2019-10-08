package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.board.BoardPlayer;
import xyz.derkades.minigames.constants.BoardConfig;

public abstract class CoinsGetTile extends CoinsTile {

	@Override
	public String getName() {
		return "Coins get";
	}

	@Override
	public String getDescription() {
		return "Get " + BoardConfig.TILE_COINS_GET_AMOUNT + " GladeCoins";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.LIGHT_PURPLE;
	}

	@Override
	public void landOnTile(final BoardPlayer player) {
		Minigames.economy.depositPlayer(player.bukkit(), BoardConfig.TILE_COINS_GET_AMOUNT);
	}

}
