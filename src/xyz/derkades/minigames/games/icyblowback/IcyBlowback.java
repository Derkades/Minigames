package xyz.derkades.minigames.games.icyblowback;

import org.bukkit.Location;

import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class IcyBlowback extends IcyBlowbackMap {

	@Override
	public Location[] getSpawnLocations() {
		return new Location[] {
			new Location(this.getWorld(), 17, 38, 17, 135f, 0f),
			new Location(this.getWorld(), 17, 38, -16, 45f, 0f),
			new Location(this.getWorld(), -16, 38, -16, -45, 0f),
			new Location(this.getWorld(), -16, 38, 17, -135f, 0f),
		};
	}

	@Override
	public String getName() {
		return "Icy Blowback";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.ICYBLOWBACK_ICYBLOWBACK;
	}

	@Override
	public int getBottomFloorLevel() {
		return 35;
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
	public String getIdentifier() {
		return "icyblowback_icyblowback";
	}

}
