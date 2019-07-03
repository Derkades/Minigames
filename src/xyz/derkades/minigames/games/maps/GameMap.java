package xyz.derkades.minigames.games.maps;

import org.bukkit.Bukkit;
import org.bukkit.World;

import xyz.derkades.minigames.worlds.GameWorld;

public abstract class GameMap {

	public abstract String getName();

	public abstract MapSize getSize();

	public abstract GameWorld getGameWorld();

	public abstract String getCredits();

	public void onPreStart() {}

	public void onStart() {}

	public void onEnd() {}

	public void onTimer(final int secondsLeft) {}

	public World getWorld() {
		if (this.getGameWorld() == null) {
			Bukkit.broadcastMessage("[WARNING] A game used the get world method without setting a world. Performing emergency shutdown to prevent damage.");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "g !");
			return null;
		} else {
			return this.getGameWorld().getWorld();
		}
	}

}
