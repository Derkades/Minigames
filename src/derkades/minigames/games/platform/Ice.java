package derkades.minigames.games.platform;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class Ice extends PlatformMap {

	@Override
	public @NotNull String getName() {
		return "Ice";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
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
	public @NotNull String getIdentifier() {
		return "platform_ice";
	}

	@Override
	Location getSpawnLocation() {
		return new Location(this.getWorld(), -32.5, 71, 10.5, -180, 0);
	}

}
