package xyz.derkades.minigames.games.oitq;

import org.bukkit.Location;

import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class Desert extends OITQMap {

	@Override
	public String getName() {
		return "Desert";
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), -27, 72, -4);
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.OITQ_DESERT;
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
	public String getIdentifier() {
		return "oneinthequiver_desert";
	}

}
