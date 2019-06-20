package xyz.derkades.minigames.games.hungergames;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Prototype extends HungerGamesMap {

	@Override
	public Location[] getStartLocations() {
		return new Location[] {
				new Location(Var.WORLD, 421.5, 69.5, 381.5),
				new Location(Var.WORLD, 424.5, 69.5, 379.5),
				new Location(Var.WORLD, 427.5, 69.5, 379.5),
				new Location(Var.WORLD, 430.5, 69.5, 379.5),
				new Location(Var.WORLD, 433.5, 69.5, 381.5),

				new Location(Var.WORLD, 433.5, 69.5, 392.5),
				new Location(Var.WORLD, 430.5, 69.5, 394.5),
				new Location(Var.WORLD, 427.5, 69.5, 394.5),
				new Location(Var.WORLD, 424.5, 69.5, 394.5),
				new Location(Var.WORLD, 421.5, 69.5, 392.5),
		};
	}

	@Override
	public Location[] getLootLevelOneLocations() {
		return new Location[] {
				new Location(Var.WORLD, 425, 68, 386),
				new Location(Var.WORLD, 426, 68, 387),
				new Location(Var.WORLD, 427, 68, 386),
				new Location(Var.WORLD, 426, 69, 386),
				new Location(Var.WORLD, 426, 68, 385),
		};
	}

	@Override
	public Location[] getLootLevelTwoLocations() {
		return new Location[] {
				new Location(Var.WORLD, 400, 68, 397),
				new Location(Var.WORLD, 441, 73, 377),
				new Location(Var.WORLD, 453, 68, 392),
		};
	}

	@Override
	public String getName() {
		return "Ugliest map ever";
	}

	@Override
	public MapSize getSize() {
		return null;
	}

	@Override
	public GameWorld getGameWorld() {
		return null;
	}


}
