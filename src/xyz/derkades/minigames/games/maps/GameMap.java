package xyz.derkades.minigames.games.maps;

import org.bukkit.World;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.worlds.GameWorld;

public abstract class GameMap {

	public abstract String getName();

	public abstract MapSize getSize();

	public abstract GameWorld getGameWorld();

	@SuppressWarnings("deprecation")
	public World getWorld() {
		if (this.getGameWorld() == null) {
			return Var.WORLD;
		} else {
			return this.getGameWorld().getWorld();
		}
	}

}
