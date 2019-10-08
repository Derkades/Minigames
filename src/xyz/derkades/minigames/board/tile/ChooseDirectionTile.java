package xyz.derkades.minigames.board.tile;

import java.util.function.Consumer;

import org.bukkit.ChatColor;

import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.minigames.board.BoardPlayer;

public abstract class ChooseDirectionTile extends DynamicDirectionTile {

	@Override
	public void moveToNextTile(final BoardPlayer player, final Consumer<Tile> onMove) {
		// Choose a random tile for now
		// TODO Implement tile choosing
		onMove.accept(ListUtils.getRandomValueFromArray(getNextTiles()));
	}

	@Override
	public void landOnTile(final BoardPlayer player) {
		// Do nothing, like a blank tile.
		// TODO Or, move player one more tile?
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
