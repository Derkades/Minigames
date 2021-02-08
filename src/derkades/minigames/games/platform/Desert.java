package derkades.minigames.games.platform;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

public class Desert extends PlatformMap {

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), 0, 65, 0);
	}

	@Override
	public String getName() {
		return "Desert";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.PLATFORM_DESERT;
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
		return "platform_desert";
	}

}