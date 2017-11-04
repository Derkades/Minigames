package xyz.derkades.minigames.games.dropper;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.BlockUtils;

public class BlackWhite extends DropperMap {

	@Override
	public String getName() {
		return "Black and White";
	}

	@Override
	public Location getLobbyLocation() {
		return new Location(Var.WORLD, 117.5, 82, 151.5, -90, 0);
	}

	@Override
	public void openDoor() {
		BlockUtils.fillArea(121, 80, 150, 123, 80, 152, Material.AIR);
	}

	@Override
	public void closeDoor() {
		BlockUtils.fillArea(121, 80, 150, 123, 80, 152, Material.WOOL);
	}

}
