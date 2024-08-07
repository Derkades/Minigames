package derkades.minigames.games;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;

import derkades.minigames.Minigames;
import derkades.minigames.Minigames.ShutdownReason;
import derkades.minigames.random.RandomlyPickable;
import derkades.minigames.utils.Disableable;
import derkades.minigames.worlds.GameWorld;

public abstract class GameMap implements RandomlyPickable, Disableable {

	public abstract String getName();

	public abstract GameWorld getGameWorld();

	public abstract String getCredits();

	public abstract String getIdentifier();

	public void onPreStart() {}

	public void onStart() {}

	public void onEnd() {}

	public void onTimer(final int secondsLeft) {}

	public World getWorld() {
		if (this.getGameWorld() == null) {
			Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "A game used the get world method without setting a game world.");
			return null;
		} else {
			return this.getGameWorld().getWorld();
		}
	}

	@Override
	public void setWeight(final double weight) {
		if (this.getIdentifier() == null) {
			Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Map identifier is null");
			return;
		}

		final String configPath = "game-voting.map." + this.getIdentifier();
		Minigames.getInstance().getConfig().set(configPath, weight);
		Minigames.getInstance().saveConfig();
	}

	@Override
	public double getWeight() {
		if (this.getIdentifier() == null) {
			Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Map identifier is null");
			return 0;
		}

		final String configPath = "game-voting.map." + this.getIdentifier();
		return Minigames.getInstance().getConfig().getDouble(configPath, 1);
	}
	
	@Override
	public boolean isDisabled() {
		return false;
	}

	private static final Map<String, GameMap> BY_IDENTIFIER = new HashMap<>();
	private static final Map<GameMap, Game<? extends GameMap>> MAP_TO_GAME = new HashMap<>();

	static {
		for (final Game<? extends GameMap> game : Game.GAMES) {
			if (game.getGameMaps() == null) {
				continue;
			}

			for (final GameMap map : game.getGameMaps()) {
				if (BY_IDENTIFIER.containsKey(map.getIdentifier())) {
					throw new IllegalStateException("Duplicate identifier " + map.getIdentifier());
//					Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Duplicate identifier " + map.getIdentifier());
//					continue;
				}
				BY_IDENTIFIER.put(map.getIdentifier(), map);
				MAP_TO_GAME.put(map, game);
			}
		}
	}

	public static GameMap fromIdentifier(final String identifier) {
		return BY_IDENTIFIER.get(identifier);
	}

	public static Game<? extends GameMap> getGame(final GameMap map) {
		return MAP_TO_GAME.get(map);
	}

}
