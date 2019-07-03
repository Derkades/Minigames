package xyz.derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import xyz.derkades.minigames.random.Size;
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
	public String getName() {
		return "Mineshaft";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.CREEPERATTACK_MINESHAFT;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return Size.SMALL;
	}

	@Override
	public String getIdentifier() {
		return "creeperattack_mineshaft";
	}

}