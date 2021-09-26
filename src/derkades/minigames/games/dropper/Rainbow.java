package derkades.minigames.games.dropper;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;
import xyz.derkades.derkutils.bukkit.BlockUtils;

class Rainbow extends DropperMap {

	@Override
	public @NotNull String getName() {
		return "Rainbow";
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
		return GameWorld.DROPPER_RAINBOW;
	}

	@Override
	public String getCredits() {
		return "Partydragen, RedstonerNor, Mr_Roky_HD, EnderAdamGaming";
	}

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "dropper_rainbow";
	}

	@Override
	Location getLobbyLocation() {
		return new Location(this.getWorld(), 0.5, 85, 0.5, -90, 0);
	}

	@Override
	void openDoor() {
		BlockUtils.fillArea(this.getWorld(), 4, 83, 1, 6, 83, -1, Material.AIR);
	}

	@Override
	void closeDoor() {
		BlockUtils.fillArea(this.getWorld(),  4, 83, 1, 6, 83, -1, Material.SLIME_BLOCK);
	}

}
