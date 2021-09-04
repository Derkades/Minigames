package derkades.minigames.games.elytra;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.worlds.GameWorld;

class Cave extends ElytraMap {

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

	@Override
	boolean isDead(final MPlayer player) {
		final Material type = player.getBlockOn().getType();
		return !player.isFlying() && (type == Material.STONE ||
				type == Material.COBBLESTONE ||
				type == Material.LAVA ||
				type == Material.ANDESITE);
	}

	@Override
	boolean hasFinished(final MPlayer player) {
		return player.getBlockOn().getType().equals(Material.LIME_WOOL);
	}

	@Override
	Location getStartLocation() {
		return new Location(this.getWorld(), 0, 65, 0, 120, 25);
	}

}
