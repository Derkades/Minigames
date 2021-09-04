package derkades.minigames.games.bowspleef;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.games.maps.GameMap;
import xyz.derkades.derkutils.bukkit.BlockUtils;

abstract class BowSpleefMap extends GameMap {

	static final BowSpleefMap[] MAPS = {
			new BowSpleefMapOriginal(),
	};

	abstract Location getSpawnLocation();

	abstract Location getLayerCenter();

	abstract int getSmallSize();

	abstract int getLargeSize();

	void fillLayer(final Location center, final int size, final Material material) {
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

	void restoreLayers() {
		fillLayer(getLayerCenter(), getLargeSize(), Material.AIR);

		fillLayer(getLayerCenter(),
				Bukkit.getOnlinePlayers().size() > 4
						? getLargeSize()
						: getSmallSize(),
				Material.TNT);
	}

}
