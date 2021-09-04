package derkades.minigames.games.oitq;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

class HouseWithFarm extends OITQMap {

	@Override
	public Location getSpawnLocation() {
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
