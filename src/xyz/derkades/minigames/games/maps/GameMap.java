package xyz.derkades.minigames.games.maps;

import org.bukkit.World;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Minigames.ShutdownReason;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.random.RandomlyPickable;
import xyz.derkades.minigames.worlds.GameWorld;

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

	public static GameMap fromIdentifier(final String identifier) {
		for (final Game<? extends GameMap> game : Game.GAMES) {
			for (final GameMap map : game.getGameMaps()) {
				if (map.getIdentifier().equalsIgnoreCase(identifier)) {
					return map;
				}
			}
		}
		return null;
	}

}
