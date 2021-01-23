package xyz.derkades.minigames.games.missile_wars;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.derkutils.bukkit.BlockUtils;
import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.utils.queue.TaskQueue;
import xyz.derkades.minigames.worlds.GameWorld;

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
		TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, 63, 56, 36, 27, 52, Material.WHITE_STAINED_GLASS));
		TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, 63, 50, 36, 27, 46, Material.LIGHT_BLUE_STAINED_GLASS));
		TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, 63, 44, 36, 27, 40, Material.BLUE_STAINED_GLASS));
		TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, 63, -45, 36, 27, -41, Material.RED_STAINED_GLASS));
		TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, 63, -51, 36, 27, -47, Material.ORANGE_STAINED_GLASS));
		TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, 63, -57, 36, 27, -53, Material.WHITE_STAINED_GLASS));
		
		for (final int z : new int[] {58, -59}) {
			TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, 27, z, 36, 27, z, Material.OBSIDIAN)); // portal neg x
			TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, 63, z, 36, 63, z, Material.OBSIDIAN)); // portal pos x
			TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -39, 27, z, -39, 63, z, Material.OBSIDIAN)); // portal upper
			TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), 36, 27, z, 36, 63, z, Material.OBSIDIAN)); // portal lower
			TaskQueue.add(() -> BlockUtils.fillArea(this.getWorld(), -38, 28, z, 35, 62, z, Material.NETHER_PORTAL)); // portal inner
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
