package xyz.derkades.minigames.board.tile;

import org.bukkit.ChatColor;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.board.BoardPlayer;

public abstract class CoinsTile extends StaticDirectionTile {

	public abstract CoinsType getType();

	public int getAmount() {
		if (getType() == CoinsType.GET)
			return 10;
		else
			return -10;
	}

	@Override
	public void landOnTile(final BoardPlayer player) {
		if (getAmount() == 0)
			return;

		if (getAmount() > 0) {
			Minigames.economy.depositPlayer(player.bukkit(), getAmount());

		} else {
			Minigames.economy.depositPlayer(player.bukkit(), -getAmount());

		}
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

	public enum CoinsType {

		GET, TAKE;

	}

}
