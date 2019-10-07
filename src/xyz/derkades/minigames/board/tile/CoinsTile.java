package xyz.derkades.minigames.board.tile;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.board.BoardPlayer;

public abstract class CoinsTile extends StaticDirectionTile {

	public abstract int getAmount();

	@Override
	public void landOnTile(final BoardPlayer player) {
		if (this.getAmount() == 0)
			return;

		if (this.getAmount() > 0) {
			Minigames.economy.depositPlayer(player.bukkit(), this.getAmount());

		} else {
			Minigames.economy.depositPlayer(player.bukkit(), -this.getAmount());

		}

	}

}
