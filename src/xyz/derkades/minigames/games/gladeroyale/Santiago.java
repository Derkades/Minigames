package xyz.derkades.minigames.games.gladeroyale;

import org.bukkit.Location;

import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class Santiago extends GladeRoyaleMap {

	@Override
	public int getWorldborderSize() {
		return 512;
	}

	@Override
	public Location getMapCenter() {
		return new Location(this.getWorld(), 1374.5, 50.0, 637);
	}

	@Override
	public String getName() {
		return "Santiago";
	}

	@Override
	public GameWorld getGameWorld() {
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
	public String getIdentifier() {
		return "gladeroyale_santiago";
	}

}
