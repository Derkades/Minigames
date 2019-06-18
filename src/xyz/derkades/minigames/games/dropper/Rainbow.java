package xyz.derkades.minigames.games.dropper;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.worlds.GameWorld;

public class Rainbow extends DropperMap {

	@Override
	public String getName() {
		return "Rainbow";
	}

	@Override
	public Location getLobbyLocation() {
		return new Location(Var.WORLD, 140.5, 82, 151.5, -90, 0);
	}

	@Override
	public void openDoor() {
		BlockUtils.fillArea(144, 80, 150, 146, 80, 152, Material.AIR);
	}

	@Override
	public void closeDoor() {
		BlockUtils.fillArea(144, 80, 150, 146, 80, 152, Material.SLIME_BLOCK);
	}

	@Override
	public MapSize getSize() {
		return null;
	}

	@Override
	public GameWorld getGameWorld() {
		return null;
	}

}
