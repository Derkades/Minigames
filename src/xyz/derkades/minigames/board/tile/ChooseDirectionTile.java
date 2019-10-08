package xyz.derkades.minigames.board.tile;

import java.util.function.Consumer;

import org.bukkit.ChatColor;

import xyz.derkades.derkutils.ListUtils;
import xyz.derkades.minigames.board.BoardPlayer;

public abstract class ChooseDirectionTile extends DynamicDirectionTile {

	@Override
	public String getName() {
		return "Choose Direction";
	}

	@Override
	public String getDescription() {
		return "Choose a direction by clicking on an item in your hotbar.";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_PURPLE;
	}

	@Override
	public void moveToNextTile(final BoardPlayer player, final Consumer<Tile> onMove) {
		// Choose a random tile for now
		// TODO Implement tile choosing
		onMove.accept(ListUtils.getRandomValueFromArray(getNextTiles()));
	}

	@Override
	public void landOnTile(final BoardPlayer player) {
		// Do nothing, like a blank tile.
		player.sendChat("landed on choose direction tile, move one more");
		player.jumpTiles(1); // it is safe to call this method, landOnTile is only executed after the player has stopped moving.
	}

}
