package xyz.derkades.minigames.board.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import xyz.derkades.derkutils.AssertionException;
import xyz.derkades.minigames.Logger;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Minigames.ShutdownReason;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.board.BoardPlayer;
import xyz.derkades.minigames.board.spectate.SpectateLocation;
import xyz.derkades.minigames.board.tile.tiles.AA;
import xyz.derkades.minigames.utils.XYZ;

public abstract class Tile {

	public static final Tile START_TILE = new AA();
	public static final Tile END_TILE = null;

	private static final Map<String, Tile> STRING_TO_TILE = new HashMap<>();

	static {
		final long millis = System.currentTimeMillis();
		final List<Tile> tiles = new ArrayList<>();
		tiles.add(START_TILE);
		while (!tiles.isEmpty()) {
			final Tile tile = tiles.remove(0);

			if (tile == null) {
				Logger.debug("Null tile!");
				continue;
			}

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

		Logger.info("Initialized string to title in %s ms", System.currentTimeMillis() - millis);
	}

	public abstract String getName();

	public abstract String getDescription();

	public abstract ChatColor getColor();

	public abstract XYZ getXYZ();

	public abstract void moveToNextTile(BoardPlayer player, Consumer<Tile> onMove);

	public abstract void landOnTile(BoardPlayer player);

	public Location getLocation() {
		return getXYZ().getLocation(Var.LOBBY_WORLD);
	}

	public SpectateLocation getSpectateLocation() {
		final String viewChar = toString().substring(0, 1);
		try {
			return SpectateLocation.valueOf(viewChar);
		} catch (final IllegalArgumentException e) {
			Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Spectate view does not exist: " + viewChar);
			throw new RuntimeException();
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof Tile) {
			final Tile tile = (Tile) other;
			return toString().equals(tile.toString());
		} else
			return false;
	}

	public static Tile fromString(final String string) {
		return STRING_TO_TILE.get(string);
	}

	public static Collection<Tile> getTiles(){
		return STRING_TO_TILE.values();
	}

}
