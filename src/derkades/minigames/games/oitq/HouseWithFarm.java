package derkades.minigames.games.oitq;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class HouseWithFarm extends OITQMap {

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), -21, 58, -3);
	}

	@Override
	public @NotNull String getName() {
		return "Farmhouse";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
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
	public @NotNull String getIdentifier() {
		return "oneinthequiver_farmhouse";
	}

}
