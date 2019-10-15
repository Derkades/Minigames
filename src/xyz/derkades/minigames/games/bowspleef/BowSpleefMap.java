package xyz.derkades.minigames.games.bowspleef;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.derkutils.bukkit.BlockUtils;
import xyz.derkades.minigames.games.maps.GameMap;

public abstract class BowSpleefMap extends GameMap {

	public static final BowSpleefMap[] MAPS = {
			new BowSpleef(),
	};

	public abstract Location getSpawnLocation();

	public abstract Location getLayerCenter();

	public abstract int getSmallSize();

	public abstract int getLargeSize();

	private void fillLayer(final Location center, final int size, final Material material) {
		final Location a = new Location(getWorld(),
				center.getX() + size,
				center.getY(),
				center.getZ() + size);

		final Location b = new Location(getWorld(),
				center.getX() - size,
				center.getY(),
				center.getZ() - size);

		BlockUtils.fillArea(a, b, material);
	}

	public void restoreLayers() {
		fillLayer(getLayerCenter(), getLargeSize(), Material.AIR);

		fillLayer(getLayerCenter(),
				Bukkit.getOnlinePlayers().size() > 3
						? getLargeSize()
						: getSmallSize(),
				Material.TNT);
	}

}
