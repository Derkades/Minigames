package xyz.derkades.minigames.games.dropper;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.worlds.GameWorld;

public class BlackWhite extends DropperMap {

	@Override
	public String getName() {
		return "Black and White";
	}

	@Override
	public Location getLobbyLocation() {
		return new Location(this.getWorld(), 0.5, 85, 0.5, -90, 0);
	}

	@Override
	public void openDoor() {
		BlockUtils.fillArea(this.getWorld(), 6, 83, -1, 4, 83, 1, Material.AIR);
	}

	@Override
	public void closeDoor() {
		BlockUtils.fillArea(this.getWorld(), 6, 83, -1, 4, 83, 1, Material.WHITE_STAINED_GLASS);
	}

	@Override
	public MapSize getSize() {
		return null;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.DROPPER_BLACKWHITE;
	}

}
