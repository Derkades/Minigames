package derkades.minigames.games;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import derkades.minigames.Minigames;
import derkades.minigames.random.RandomlyPickable;
import derkades.minigames.utils.Disableable;
import derkades.minigames.worlds.GameWorld;

public abstract class GameMap implements RandomlyPickable, Disableable {

	@NotNull
	public abstract String getName();

	@NotNull
	public abstract GameWorld getGameWorld();

	@Nullable
	public abstract String getCredits();

	@NotNull
	public abstract String getIdentifier();

	public void onPreStart() {}

	public void onStart() {}

	public void onEnd() {}

	public void onTimer(final int secondsLeft) {}

	public World getWorld() {
		return this.getGameWorld().getWorld();
	}

	@Override
	public void setWeight(final double weight) {
		final String configPath = "game-voting.map." + this.getIdentifier();
		Minigames.getInstance().getConfig().set(configPath, weight);
		Minigames.getInstance().saveConfig();
	}

	@Override
	public double getWeight() {
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
			for (final GameMap map : game.getGameMaps()) {
				if (BY_IDENTIFIER.containsKey(map.getIdentifier())) {
					throw new IllegalStateException("Duplicate identifier " + map.getIdentifier());
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
