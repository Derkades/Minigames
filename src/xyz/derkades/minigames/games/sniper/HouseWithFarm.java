package xyz.derkades.minigames.games.sniper;

import org.bukkit.Location;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;

public class HouseWithFarm extends SniperMap {

	@Override
	public Location getSpawnLocation() {
		return new Location(Var.WORLD, 183.5, 86, 213.5);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 183.5, 86, 213.5);
	}

	@Override
	public String getName() {
		return "Farmhouse";
	}

	@Override
	public MapSize getSize() {
		return MapSize.NORMAL;
	}

}
