package derkades.minigames.games.maps;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;

import derkades.minigames.Minigames;
import derkades.minigames.Minigames.ShutdownReason;
import derkades.minigames.games.Game;
import derkades.minigames.random.RandomlyPickable;
import derkades.minigames.worlds.GameWorld;

public abstract class GameMap implements RandomlyPickable {

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

	private static final Map<String, GameMap> BY_IDENTIFIER = new HashMap<>();

	public static final void init() {
		for (final Game<? extends GameMap> game : Game.GAMES) {
			for (final GameMap map : game.getGameMaps()) {
				if (BY_IDENTIFIER.containsKey(map.getIdentifier())) {
					Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "Duplicate identifier " + map.getIdentifier());
					continue;
				}
				BY_IDENTIFIER.put(map.getIdentifier(), map);
			}
		}
	}

	public static GameMap fromIdentifier(final String identifier) {
		return BY_IDENTIFIER.get(identifier);
	}

}
