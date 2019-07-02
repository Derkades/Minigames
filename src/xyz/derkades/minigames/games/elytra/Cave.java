package xyz.derkades.minigames.games.elytra;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.worlds.GameWorld;

public class Cave extends ElytraMap {

	@Override
	public boolean isDead(final MPlayer player) {
		final Material type = player.getBlockOn().getType();
		return !player.isFlying() && (type == Material.STONE ||
				type == Material.COBBLESTONE ||
				type == Material.LAVA ||
				type == Material.ANDESITE);
	}

	@Override
	public boolean hasFinished(final MPlayer player) {
		return player.getBlockOn().getType().equals(Material.LIME_WOOL);
	}

	@Override
	public Location getStartLocation() {
		return new Location(this.getWorld(), 163.5, 76.5, 339.5, 120, 25);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(this.getWorld(), 151.5, 76, 343.5);
	}

	@Override
	public String getName() {
		return "Cave";
	}

	@Override
	public MapSize getSize() {
		return null;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.ELYTRA_CAVE;
	}

	@Override
	public String getCredits() {
		return null;
	}

}
