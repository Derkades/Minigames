package xyz.derkades.minigames.games.oitq;

import org.bukkit.Location;

import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class Snow extends OITQMap {

	@Override
	public String getName() {
		return "Snow";
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), -22, 66, -1);
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.OITQ_SNOW;
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
		return "oneinthequiver_snow";
	}

}