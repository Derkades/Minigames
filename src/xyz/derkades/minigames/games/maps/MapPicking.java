package xyz.derkades.minigames.games.maps;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import xyz.derkades.minigames.utils.Utils;

public class MapPicking {

	public static GameMap pickRandomMap(final GameMap[] maps) {
		final Map<GameMap, Double> weightedList = new HashMap<>();

		// Populate hashmap
		for (final GameMap map : maps) {
			final MapSize size = map.getSize();
			final int online = Bukkit.getOnlinePlayers().size();

			final double weight;

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

			weightedList.put(map, weight);
		}


		return Utils.getWeightedRandom(weightedList);
	}


}
