package xyz.derkades.minigames.games.elytra;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.random.Size;
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
		return new Location(this.getWorld(), 0, 65, 0, 120, 25);
	}

	@Override
	public String getName() {
		return "Cave";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.ELYTRA_CAVE;
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
		return "elytra_cave";
	}

}
