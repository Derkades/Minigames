package xyz.derkades.minigames.games.maps;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.utils.Utils;

public class MapPicking {

	public static GameMap pickRandomMap(final Game<? extends GameMap> game) {
		if (Minigames.NEXT_MAP != null) {
			final String next = Minigames.NEXT_MAP;
			Minigames.NEXT_MAP = null;
			for (final GameMap map : game.getGameMaps()) {
				if (map.getName().equalsIgnoreCase(next)) {
					return map;
				}
			}
			Bukkit.broadcastMessage("Map was forced to " + next + " but it doesn't exist");
		}

		final Map<GameMap, Double> weightedList = new HashMap<>();

		// Populate hashmap
		for (final GameMap map : game.getGameMaps()) {
			final MapSize size = map.getSize();
			final int online = Bukkit.getOnlinePlayers().size();

			double weight;

			if (size == MapSize.SMALL) {
				if (online < 5) {
					weight = 2;
				} else if (online > 6) {
					weight = 0.5;
				} else {
					weight = 1;
				}
			} else if (size == MapSize.NORMAL) {
				if (online < 4 || online > 6) {
					weight = 1;
				} else {
					weight = 2;
				}
			} else if (size == MapSize.LARGE) {
				if (online > 5) {
					weight = 3;
				} else {
					weight = 1;
				}
			} else {
				weight = 1;
			}

			final String configPath = "game-voting.map." + game.getName() + "." + map.getName();
			final double multiplier = Minigames.getInstance().getConfig().getDouble(configPath, 1);
			weight *= multiplier;

			weightedList.put(map, weight);
		}


		return Utils.getWeightedRandom(weightedList);
	}


}
