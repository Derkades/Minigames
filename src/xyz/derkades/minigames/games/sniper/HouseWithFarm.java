package xyz.derkades.minigames.games.sniper;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class HouseWithFarm extends SniperMap {

	@Override
	public Location getSpawnLocation() {
		//return new Location(Var.WORLD, 183.5, 86, 213.5);
		return new Location(this.getWorld(), -21, 58, -3);
	}

	@Override
	public String getName() {
		return "Farmhouse";
	}

	@Override
	public MapSize getSize() {
		return MapSize.NORMAL;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.OITQ_FARMHOUSE;
	}

}
