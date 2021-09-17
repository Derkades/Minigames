package derkades.minigames.random;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

import derkades.minigames.Logger;
import derkades.minigames.games.Game;
import derkades.minigames.games.GameMap;
import derkades.minigames.utils.Disableable;
import derkades.minigames.utils.Utils;

public class RandomPicking {

	public static Game<? extends GameMap> FORCE_GAME = null;
	public static GameMap FORCE_MAP = null;

	@SuppressWarnings("unchecked")
	public static Game<? extends GameMap> getRandomGame() {
		final Game<? extends GameMap> game;
		if (FORCE_GAME != null) {
			game = FORCE_GAME;
			FORCE_GAME = null;
			Logger.debug("Not picking a random game, forced game ", game);
		} else {
			game = (Game<? extends GameMap>) getRandom(Arrays.asList(Game.GAMES));
			Logger.debug("Picked random game ", game);
		}

		return game;
	}

	public static GameMap getRandomMap(final List<GameMap> maps) {
		final GameMap map;
		if (FORCE_MAP != null) {
			map = FORCE_MAP;
			FORCE_MAP = null;
			Logger.debug("Not picking a random map, forced map ", map);
		} else {
			map = (GameMap) getRandom(maps);
			Logger.debug("Picked random map ", map);
		}

		return map;
	}

	private static RandomlyPickable getRandom(final List<? extends RandomlyPickable> list) {
		final Map<RandomlyPickable, Double> weightedList = new HashMap<>();

		// Populate hashmap
		for (final RandomlyPickable randomlyPickableThing : list) {
			if (randomlyPickableThing instanceof Disableable) {
				if (((Disableable) randomlyPickableThing).isDisabled()) {
					continue;
				}
			}
			
			final Size size = randomlyPickableThing.getSize();
			final int online = Bukkit.getOnlinePlayers().size();

			double multiplier = randomlyPickableThing.getWeight();

			if (size == Size.SMALL) {
				if (online < 4) {
					multiplier *= 2; // Small with few players, high chance
				} else if (online > 6) {
					multiplier *= 0.5; // Small with many players, low chance
				} else {
					// Small with medium players, normal chance
				}
			} else if (size == Size.NORMAL) {
				if (online < 4 || online > 6) {
					multiplier *= 1; // Normal size with few or many players, normal chance
				} else {
					multiplier *= 2; // Normal size with normal players, high chance
				}
			} else if (size == Size.LARGE) {
				if (online > 4) {
					multiplier *= 2; // Large size with many players, high chance
				} else if (online < 3) {
					multiplier *= 0.5; // Large size with very few players, low chance
				} else {
					multiplier *= 1; // Large size with normal players, normal chance
				}
			} else if (size == Size.ADAPTIVE) {
				multiplier *= 1;
			} else {
				multiplier *= 1; // Unknown size (e.g. if it's set to null), normal chance.
			}

			weightedList.put(randomlyPickableThing, multiplier);
		}


		return Utils.getWeightedRandom(weightedList);
	}

}
