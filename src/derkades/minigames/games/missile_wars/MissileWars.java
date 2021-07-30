package derkades.minigames.games.missile_wars;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.utils.queue.TaskQueue;
import derkades.minigames.worlds.GameWorld;
import xyz.derkades.derkutils.bukkit.BlockUtils;

public class MissileWars extends MissileWarsMap {

	@Override
	public Size getSize() {
		return Size.NORMAL;
	}

	@Override
	public String getName() {
		return "Missile Wars";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.MISSILES_PROTOTYPE;
	}

	@Override
	public String getCredits() {
		return "ben_the_bunny, Corrupt_World";
	}

	@Override
	public String getIdentifier() {
		return "missiles_prototype";
	}

	@Override
	public void buildArena() {
		TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, 63, 62, 36, 27, 57, Material.WHITE_STAINED_GLASS));
		TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, 63, 55, 36, 27, 50, Material.LIGHT_BLUE_STAINED_GLASS));
		TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, 63, 48, 36, 27, 43, Material.BLUE_STAINED_GLASS));

		TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, 63, -63, 36, 27, -58, Material.WHITE_STAINED_GLASS));
		TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, 63, -56, 36, 27, -51, Material.ORANGE_STAINED_GLASS));
		TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, 63, -49, 36, 27, -44, Material.RED_STAINED_GLASS));

		for (final int z : new int[] { 66, -67 }) {
			for (final int x : new int[] { -39, 36}) {
				TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), x, 28, z, x, 62, z, Material.OBSIDIAN));
			}

			for (final int y : new int[] { 27, 33, 39, 45, 51, 57, 63}) {
				TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, y, z, 36, y, z, Material.OBSIDIAN));
			}
			TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -38, 28, z, 35, 62, z, Material.NETHER_PORTAL,
					(b) -> !b.getType().equals(Material.OBSIDIAN))); // portal inner
		}
	}

	@Override
	public Location getArenaBorderMin() {
		return new Location(this.getWorld(), -100, 0, -100);
	}

	@Override
	public Location getArenaBorderMax() {
		return new Location(this.getWorld(), 100, 110, 100);
	}

	@Override
	public Location getTeamRedSpawnLocation() {
		return new Location(this.getWorld(), -1.0, 65, -54, 0, 0);
	}

	@Override
	public Location getTeamBlueSpawnLocation() {
		return new Location(this.getWorld(), -1.0, 65, 54, 180, 0);
	}

}
