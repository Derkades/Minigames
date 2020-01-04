package xyz.derkades.minigames.games.bowspleef;

import org.bukkit.Location;

import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class BowSpleef extends BowSpleefMap {

	@Override
	public Size getSize() {
		return Size.ADAPTIVE;
	}

	@Override
	public String getName() {
		return "Bow Spleef";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.BOWSPLEEF_BOWSPLEEF;
	}

	@Override
	public String getCredits() {
		return "Corrupt_World";
	}

	@Override
	public String getIdentifier() {
		return "bowspleef_bowspleef";
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(getWorld(), 0, 65, 0);
	}

	@Override
	public int getSmallSize() {
		return 7;
	}

	@Override
	public int getLargeSize() {
		return 16;
	}

	@Override
	public Location getLayerCenter() {
		return new Location(getWorld(), 0, 64, 0);
	}

}
