package derkades.minigames.games.icyblowback;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class IcyBlowbackMapImpl extends IcyBlowbackMap {

	@Override
	public @NotNull String getName() {
		return "Icy Blowback";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.ICYBLOWBACK_ICYBLOWBACK;
	}



	@Override
	public String getCredits() {
		return "Yaraka";
	}

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "icyblowback_icyblowback";
	}

	@Override
	Location[] getSpawnLocations() {
		return new Location[] {
			new Location(this.getWorld(), 17.5, 38, 17.5, 135f, 0f),
			new Location(this.getWorld(), 17.5, 38, -16.5, 45f, 0f),
			new Location(this.getWorld(), -16.5, 38, -16.5, -45f, 0f),
			new Location(this.getWorld(), -16.5, 38, 17.5, -135f, 0f),
		};
	}

	@Override
	int getBottomFloorLevel() {
		return 35;
	}

}
