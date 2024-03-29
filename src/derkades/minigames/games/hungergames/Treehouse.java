package derkades.minigames.games.hungergames;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class Treehouse extends HungerGamesMap {

	@Override
	public @NotNull String getName() {
		return "Treehouse";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.HG_TREEHOUSE;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return Size.NORMAL;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "hungergames_treehouse";
	}

	@Override
	Location[] getStartLocations() {
		return new Location[] {
				new Location(this.getWorld(), -29.5, 49.5, -68.5),
				new Location(this.getWorld(), -34.5, 49.5, -63.5),
				new Location(this.getWorld(), -37.5, 49.5, -60.5),
				new Location(this.getWorld(), -35.5, 49.5, -55.5),
				new Location(this.getWorld(), -30.5, 49.5, -53.5),
				new Location(this.getWorld(), -25.5, 49.5, -51.5),
				new Location(this.getWorld(), -20.5, 49.5, -53.5),
				new Location(this.getWorld(), -19.5, 49.5, -60.5),
				new Location(this.getWorld(), -21.5, 49.5, -64.5),
				new Location(this.getWorld(), -23.5, 49.5, -69.5),
		};
	}

	@Override
	Location[] getLootLevelOneLocations() {
		return new Location[] {
				// Middle tree
				new Location(this.getWorld(), -29, 48, -60),
				new Location(this.getWorld(), -27, 48, -61),
				new Location(this.getWorld(), -28, 49, -62),
				new Location(this.getWorld(), -29, 48, -60),

				// Cave
				new Location(this.getWorld(), -16, 34, -42),
				new Location(this.getWorld(), -15, 32, -43),
				new Location(this.getWorld(), -14, 32, -45),
				new Location(this.getWorld(), -15, 32, -46),
				new Location(this.getWorld(), -16, 33, -47),
				new Location(this.getWorld(), -18, 33, -45),

				// Treehouse
				new Location(this.getWorld(), -26, 58, -61),
				new Location(this.getWorld(), -26, 58, -59),
				new Location(this.getWorld(), -27, 59, -59),
				new Location(this.getWorld(), -28, 58, -59),
				new Location(this.getWorld(), -30, 58, -60),
		};
	}

	@Override
	Location[] getLootLevelTwoLocations() {
		return new Location[] {
				// Trapped chest
				new Location(this.getWorld(), -45, 56, -91),

				// Chest in wooden house
				new Location(this.getWorld(), -20, 54, -98),

				// Chests under trees
				new Location(this.getWorld(), -27, 50, -87),
				new Location(this.getWorld(), -13, 50, -90),
				new Location(this.getWorld(), -7, 52, -82),
				new Location(this.getWorld(), -12, 49, -54),

				// Chests on/near mountains
				new Location(this.getWorld(), -52, 52, -55),
				new Location(this.getWorld(), -45, 59, -71),
				new Location(this.getWorld(), -40, 58, -79),
				new Location(this.getWorld(), -45, 56, -91),
				new Location(this.getWorld(), -7, 58, -64),
				new Location(this.getWorld(), 5, 56, -52),
				new Location(this.getWorld(), -23, 58, -40),
				new Location(this.getWorld(), -4, 54, -39),
				new Location(this.getWorld(), -36, 50, -46),
		};
	}

	@Override
	Location getCenterLocation() {
		return new Location(this.getWorld(), -27.5, 50, -62.5);
	}

	@Override
	double getMaxBorderSize() {
		return 100;
	}

	@Override
	double getMinBorderSize() {
		return 35;
	}

}
