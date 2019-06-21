package xyz.derkades.minigames.games.elytra;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.worlds.GameWorld;

public class Cave extends ElytraMap {

	@Override
	public boolean isDead(final MPlayer player) {
		final Material type = player.getBlockOn().getType();
		return !player.isFlying() && (type == Material.STONE || type == Material.COBBLESTONE || type == Material.LAVA);
	}

	@Override
	public boolean hasFinished(final MPlayer player) {
		return player.getBlockOn().getType().equals(Material.LIME_WOOL);
	}

	@Override
	public boolean isSafeOnSpawnPlatform(final MPlayer player) {
		final Material type = player.getBlockOn().getType();
		return type.equals(Material.OAK_PLANKS) || type.equals(Material.OAK_LOG) || type.equals(Material.OAK_STAIRS);
	}

	@Override
	public Location getStartLocation() {
		return new Location(Var.WORLD, 163.5, 76.5, 339.5, 120, 25);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 151.5, 76, 343.5);
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
		return null;
	}

}
