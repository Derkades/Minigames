package derkades.minigames.games.digdug;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;

public class Prototype extends DigDugMap {

	@Override
	public Location getBlocksMinLocation() {
		return new Location(this.getWorld(), -17, 44, -19);
	}

	@Override
	public Location getBlocksMaxLocation() {
		return new Location(this.getWorld(), 22, 64, 17);
	}

	@Override
	public Location getSpawnLocation() {
		return new Location(this.getWorld(), 0, 65, 0);
	}

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.DIGDUG_PROTOTYPE;
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
		return "digdug_prototype";
	}

}
