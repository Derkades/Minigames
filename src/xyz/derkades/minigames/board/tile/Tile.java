package xyz.derkades.minigames.board.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Location;

import xyz.derkades.derkutils.AssertionException;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.board.BoardPlayer;
import xyz.derkades.minigames.board.tile.tiles.AA;
import xyz.derkades.minigames.utils.XYZ;

public abstract class Tile {

	public static final Tile START_TILE = new AA();
	public static final Tile END_TILE = null;

	public static final int TILE_TELEPORT_DELAY = 10;

	private static final Map<String, Tile> STRING_TO_TILE = new HashMap<>();

	static {
		final List<Tile> tiles = new ArrayList<>();
		tiles.add(START_TILE);
		while (!tiles.isEmpty()) {
			final Tile tile = tiles.get(0);

			STRING_TO_TILE.put(tile.toString(), tile);

			if (tile instanceof DynamicDirectionTile) {
				final DynamicDirectionTile dynamicTile = (DynamicDirectionTile) tile;
				tiles.addAll(Arrays.asList(dynamicTile.getNextTiles()));
			} else if (tile instanceof StaticDirectionTile) {
				final StaticDirectionTile staticTile = (StaticDirectionTile) tile;
				tiles.add(staticTile.getNextTile());
			} else
				throw new AssertionException();
		}
	}

	public abstract XYZ getXYZ();

	public abstract void moveToNextTile(BoardPlayer player, Consumer<Tile> onMove);

	public abstract void landOnTile(BoardPlayer player);

	public Location getLocation() {
		return this.getXYZ().getLocation(Var.LOBBY_WORLD);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof Tile) {
			final Tile tile = (Tile) other;
			return this.toString().equals(tile.toString());
		} else
			return false;
	}

	public static Tile fromString(final String string) {
		return STRING_TO_TILE.get(string);
	}

}
