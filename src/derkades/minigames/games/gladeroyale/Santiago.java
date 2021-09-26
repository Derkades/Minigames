package derkades.minigames.games.gladeroyale;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class Santiago extends GladeRoyaleMap {

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.MGR_SANTIAGO;
	}

	@Override
	public String getCredits() {
		return "'Santiago Valley' by Juancy on Planet Minecraft";
	}

	@Override
	public Size getSize() {
		return Size.LARGE;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "gladeroyale_santiago";
	}

	@Override
	int getWorldborderSize() {
		return 512;
	}

	@Override
	Location getMapCenter() {
		return new Location(this.getWorld(), 1374.5, 50.0, 637);
	}

	@Override
	public @NotNull String getName() {
		return "Santiago";
	}

	@Override
	int getMinY() {
		return 30;
	}

	@Override
	int getMaxY() {
		return 125;
	}

}
