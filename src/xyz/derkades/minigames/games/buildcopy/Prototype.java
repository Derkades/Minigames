package xyz.derkades.minigames.games.buildcopy;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class Prototype extends BuildCopyMap {
	
	@Override
	public Size getSize() {
		return Size.ADAPTIVE;
	}

	@Override
	public int getSupportedPlayerCount() {
		return 10;
	}

	@Override
	public Location getSpawnLocation(final int position) {
		return new Location(this.getWorld(), 8.5 - 4*position, 65, 0, 0, 0);
	}

	@Override
	public void buildOriginal(final int position, final Material[] materials) {
		final Location loc = this.getSpawnLocation(position);
		final int z = -6;
		int i = 0;
		for (int x = loc.getBlockX() - 1; x <= loc.getBlockX() + 1; x++) {
			for (int y = loc.getBlockY(); y <= loc.getBlockY() + 2; y++) {
				new Location(loc.getWorld(), x, y, z).getBlock().setType(materials[i++]);
			}
		}
	}

	@Override
	public void clearCopy(final int position) {
		final Location loc = this.getSpawnLocation(position);
		final int z = 2;
		for (int x = loc.getBlockX() - 1; x <= loc.getBlockX() + 1; x++) {
			for (int y = loc.getBlockY(); y <= loc.getBlockY() + 2; y++) {
				new Location(loc.getWorld(), x, y, z).getBlock().setType(Material.AIR);
			}
		}
	}

	@Override
	public boolean checkCopy(final int position, final Material[] materials) {
		final Location loc = this.getSpawnLocation(position);
		final int z = 2;
		int i = 0;
		for (int x = loc.getBlockX() - 1; x <= loc.getBlockX() + 1; x++) {
			for (int y = loc.getBlockY(); y <= loc.getBlockY() + 2; y++) {
				if (new Location(loc.getWorld(), x, y, z).getBlock().getType() != materials[i++]) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.BUILDCOPY_PROTOTYPE;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return "buildcopy_prototype";
	}

}
