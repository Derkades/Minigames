package xyz.derkades.minigames.random;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.games.maps.GameMap;
import xyz.derkades.minigames.utils.Utils;

public class RandomPicking {

	public static String FORCE_GAME = null;
	public static String FORCE_MAP = null;

	@SuppressWarnings("unchecked")
	public static Game<? extends GameMap> getRandomGame() {
		if (FORCE_GAME != null) {
			final Game<? extends GameMap> map = Game.fromString(FORCE_GAME);
			FORCE_GAME = null;
			if (map != null) {
				return map;
			} else {
				Bukkit.broadcastMessage("A game was forced, but the provided identifier is invalid.");
			}
		}


		return (Game<? extends GameMap>) getRandom(Arrays.asList(Game.GAMES));
	}

	public static GameMap getRandomMap(final List<GameMap> maps) {
		if (FORCE_MAP != null) {
			final GameMap map = GameMap.fromIdentifier(FORCE_MAP);
			FORCE_MAP = null;
			if (map != null) {
				return map;
			} else {
				Bukkit.broadcastMessage("A map was forced, but the provided identifier is invalid.");
			}
		}

		return (GameMap) getRandom(maps);
	}

	private static RandomlyPickable getRandom(final List<? extends RandomlyPickable> list) {
		final Map<RandomlyPickable, Double> weightedList = new HashMap<>();

		// Populate hashmap
		for (final RandomlyPickable randomlyPickableThing : list) {
			final Size size = randomlyPickableThing.getSize();
			final int online = Bukkit.getOnlinePlayers().size();

			double multiplier = randomlyPickableThing.getWeight();

			if (size == Size.SMALL) {
				if (online < 5) {
					multiplier *= 2; // Small with few players, high chance
				} else if (online > 6) {
					multiplier *= 0.5; // Small with many players, low chance
				} else {
					multiplier *= 1; // Small with medium players, normal chance
				}
			} else if (size == Size.NORMAL) {
				if (online < 4 || online > 6) {
					multiplier *= 1; // Normal size with few or many players, normal chance
				} else {
					multiplier *= 2; // Normal size with normal players, high chance
				}
			} else if (size == Size.LARGE) {
				if (online > 5) {
					multiplier *= 3; // Large size with many players, high chance
				} else if (online < 3) {
					multiplier *= 0.5; // Large size with very few players, low chance
				} else {
					multiplier *= 1; // Large size with normal players, normal chance
				}
			} else {
				multiplier *= 1; // Unknown size (e.g. if it's set to null), normal chance.
			}

			weightedList.put(randomlyPickableThing, multiplier);
		}


		return Utils.getWeightedRandom(weightedList);
	}

}
