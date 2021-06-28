package derkades.minigames.games.murderymister;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

public class DeckedOutCastle extends MurderyMisterMap {

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	public Location[] getSpawnLocations() {
		return new Location[] {
				new Location(this.getWorld(), -1.5, 89, 118.5),
				new Location(this.getWorld(), -3.5, 87, 151.5),
				new Location(this.getWorld(), -15.5, 89, 146.5),
				new Location(this.getWorld(), -23.5, 91, 147.5),
				new Location(this.getWorld(), -38.5, 89, 132.5),
				new Location(this.getWorld(), -27.5, 89, 117.5),
				new Location(this.getWorld(), -5.5, 86, 148.5),
				new Location(this.getWorld(), -7.5, 89, 117.5),
				new Location(this.getWorld(), -6.5, 89, 141.5),
				new Location(this.getWorld(), -21.5, 89, 122.5),
		};
	}

	@Override
	public Location[] getFlickeringRedstoneLamps() {
		return null;
	}

	@Override
	public Location[] getCandles() {
		return null;
	}

	@Override
	public String getName() {
		return "Decked Out (Castle)";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.MM_DECKEDOUT;
	}

	@Override
	public String getCredits() {
		return "Hermitcraft";
	}

	@Override
	public String getIdentifier() {
		return "murderymister_deckedoutcastle";
	}

}
