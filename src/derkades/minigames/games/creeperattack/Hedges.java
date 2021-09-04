package derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

class Hedges extends CreeperAttackMap {

	@Override
	Location getCreeperLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

	@Override
	Location getSpawnLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

	@Override
	public String getName() {
		return "Hedges";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.CREEPERATTACK_HEDGES;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return Size.NORMAL;
	}

	@Override
	public String getIdentifier() {
		return "creeperattack_hedges";
	}

}
