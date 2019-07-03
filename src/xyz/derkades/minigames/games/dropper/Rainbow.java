package xyz.derkades.minigames.games.dropper;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.worlds.GameWorld;

public class Rainbow extends DropperMap {

	@Override
	public String getName() {
		return "Rainbow";
	}

	@Override
	public Location getLobbyLocation() {
		return new Location(this.getWorld(), 0.5, 85, 0.5, -90, 0);
	}

	@Override
	public void openDoor() {
		BlockUtils.fillArea(this.getWorld(), 4, 83, 1, 6, 83, -1, Material.AIR);
	}

	@Override
	public void closeDoor() {
		BlockUtils.fillArea(this.getWorld(),  4, 83, 1, 6, 83, -1, Material.SLIME_BLOCK);
	}

	@Override
	public GameWorld getGameWorld() {
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
	public String getIdentifier() {
		return "dropper_rainbow";
	}

}
