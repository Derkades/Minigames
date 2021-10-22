package derkades.minigames.games.studyfall;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.derkades.derkutils.bukkit.BlockUtils;

public class Prototype extends StudyFallMap {

	@Override
	public @NotNull String getName() {
		return "Study Fall";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.STUDYFALL_PROTOTYPE;
	}

	@Override
	public @Nullable String getCredits() {
		return null;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "studyfall_prototype";
	}

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	void clearGlass() {
		BlockUtils.fillArea(this.getWorld(), 5, 50, 13, -6, 93, 13, Material.AIR);
	}

	@Override
	Location getPreSpawnLocation() {
		return new Location(this.getWorld(), 0, 70, 0);
	}

	@Override
	Location getSpawnLocation() {
		return new Location(this.getWorld(), -0.5, 95, 17.5, 180, 0);
	}
}
