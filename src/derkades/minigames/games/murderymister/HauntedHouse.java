package derkades.minigames.games.murderymister;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class HauntedHouse extends MurderyMisterMap {

	@Override
	public @NotNull String getName() {
		return "Haunted House";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.MM_HAUNTEDHOUSE;
	}

	@Override
	public String getCredits() {
		return "Corrupt_World";
	}

	@Override
	public Size getSize() {
		return Size.NORMAL;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "murderymister_hauntedhouse";
	}

	@Override
	Location[] getSpawnLocations() {
		return new Location[] {
				new Location(this.getWorld(), -23.5, 63.5, -13.5),
				new Location(this.getWorld(), 13.5, 66.0, 18.5),
				new Location(this.getWorld(), 1.5, 75.0, 2.5),
				new Location(this.getWorld(), -3.5, 84, -2.5),
				new Location(this.getWorld(), 3.5, 94, 2.5),
				new Location(this.getWorld(), -0.5, 100, 4.5),
				new Location(this.getWorld(), 2.5, 90, 6.5),
				new Location(this.getWorld(), 4.5, 94, 1.5),
				new Location(this.getWorld(), -6.5, 92.0, 4.5),
				new Location(this.getWorld(), 4.5, 79, -1.5),
		};
	}

	@Override
	Location[] getFlickeringRedstoneLamps() {
		return new Location[] {
				new Location(this.getWorld(), -8, 72, 5),
				new Location(this.getWorld(), 3, 77, -6),
		};
	}

	@Override
	Location[] getCandles() {
		return new Location[] {
				new Location(this.getWorld(), -1, 82, 0),
				new Location(this.getWorld(), 0, 82, -1),
				new Location(this.getWorld(), 1, 82, 0),
				new Location(this.getWorld(), 0, 82, 1),
				new Location(this.getWorld(), -1, 71, -1),
				new Location(this.getWorld(), -2, 71, -2),
				new Location(this.getWorld(), -2, 71, 0),
				new Location(this.getWorld(), -3, 71, -1),
		};
	}

}
