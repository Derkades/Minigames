package xyz.derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Mineshaft extends CreeperAttackMap {

	@Override
	public Location getCreeperLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);

	}

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5, -180f, 0f);
	}

	@Override
	public Location getSpectatorLocation() {
		return null;
	}

	@Override
	public String getName() {
		return "Mineshaft";
	}

	@Override
	public MapSize getSize() {
		return MapSize.SMALL;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.CREEPERATTACK_MINESHAFT;
	}

}