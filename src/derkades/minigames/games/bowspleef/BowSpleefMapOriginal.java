package derkades.minigames.games.bowspleef;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

class BowSpleefMapOriginal extends BowSpleefMap {

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
	Location getSpawnLocation() {
		return new Location(getWorld(), 0, 65, 0);
	}

	@Override
	int getSmallSize() {
		return 7;
	}

	@Override
	int getLargeSize() {
		return 16;
	}

	@Override
	Location getLayerCenter() {
		return new Location(getWorld(), 0, 64, 0);
	}

}
