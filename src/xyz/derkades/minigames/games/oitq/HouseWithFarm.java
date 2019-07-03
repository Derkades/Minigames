package xyz.derkades.minigames.games.oitq;

import org.bukkit.Location;

import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class HouseWithFarm extends OITQMap {

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
	public GameWorld getGameWorld() {
		return GameWorld.OITQ_FARMHOUSE;
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
		return "oneinthequiver_farmhouse";
	}

}
