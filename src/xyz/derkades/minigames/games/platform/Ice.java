package xyz.derkades.minigames.games.platform;

import org.bukkit.Location;

import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class Ice extends PlatformMap {

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), -32.5, 71, 10.5, -180, 0);
	}

	@Override
	public String getName() {
		return "Ice";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.PLATFORM_ICE;
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
	public String getIdentifier() {
		return "platform_ice";
	}

}
