package derkades.minigames.games.digdug;

import org.bukkit.Location;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

public class Prototype extends DigDugMap {

	@Override
	public @NotNull String getName() {
		return "Prototype";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
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
	public @NotNull String getIdentifier() {
		return "digdug_prototype";
	}

	@Override
	Location getBlocksMinLocation() {
		return new Location(this.getWorld(), -17, 44, -19);
	}

	@Override
	Location getBlocksMaxLocation() {
		return new Location(this.getWorld(), 22, 64, 17);
	}

	@Override
	Location getSpawnLocation() {
		return new Location(this.getWorld(), 0, 65, 0);
	}

}
