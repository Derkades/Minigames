package derkades.minigames.games.meteorshower;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.derkades.derkutils.bukkit.BlockUtils;

public class Prototype extends MeteorShowerMap {

	@Override
	public @NotNull String getName() {
		return "Prototype";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.METEORSHOWER_PROTOTYPE;
	}

	@Override
	public @Nullable String getCredits() {
		return null;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "meteorshower_prototype";
	}

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	public void onPreStart() {
		BlockUtils.fillArea(this.getWorld(), -20, 62, -20, 20, 63, 20, Material.CALCITE);
	}

	@Override
	MeteorBounds getMeteorBounds() {
		return new MeteorBounds(-20, 20, 110, -20, 20);
	}

	@Override
	Location getSpawnLocation() {
		return new Location(this.getWorld(), 0.5, 64, 0.5);
	}
}
