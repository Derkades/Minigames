package derkades.minigames.games.creeperattack;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

public class DeckedOutForest extends CreeperAttackMap {

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	public Location getCreeperLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5);
	}

	@Override
	public String getName() {
		return "Decked Out (Forest)";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.CREEPERATTACK_DECKEDOUTFOREST;
	}

	@Override
	public String getCredits() {
		return "Hermitcraft";
	}

	@Override
	public String getIdentifier() {
		return "creeperattack_deckedoutforest";
	}

	@Override
	public void onStart() {
		this.getWorld().setTime(18000);
	}

}
