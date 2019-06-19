package xyz.derkades.minigames.games.digdug;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Prototype extends DigDugMap {

	@Override
	public Location getBlocksMinLocation() {
		return new Location(this.getWorld(), -17, 44, -19);
	}

	@Override
	public Location getBlocksMaxLocation() {
		return new Location(this.getWorld(), 22, 64, 17);
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), 0, 65, 0);
	}

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public MapSize getSize() {
		return MapSize.NORMAL;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.DIGDUG_PROTOTYPE;
	}

}
