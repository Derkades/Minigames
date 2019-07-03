package xyz.derkades.minigames.games.snowfight;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class Maze extends SnowFightMap {

	@Override
	public Location getSpawnLocation() {
		return new Location(Var.WORLD, 213.5, 46, 293.5);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 232.5, 46, 299.0, 180, 0);
	}

	@Override
	public String getName() {
		return "Maze";
	}

	@Override
	public GameWorld getGameWorld() {
		return null;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return Size.SMALL;
	}

	@Override
	public String getIdentifier() {
		return "snowfight_maze";
	}

}
