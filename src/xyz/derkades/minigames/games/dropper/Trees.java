package xyz.derkades.minigames.games.dropper;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.BlockUtils;

public class Trees extends DropperMap {

	@Override
	public String getName() {
		return "Trees";
	}

	@Override
	public Location getLobbyLocation() {
		return new Location(Var.WORLD, 163.5, 84, 151.5, -90, 0);
	}

	@Override
	public void openDoor() {
		fillDoor(Material.AIR);
	}
	
	@Override
	public void closeDoor() {
		fillDoor(Material.STAINED_GLASS);
	}
	
	private void fillDoor(Material material) {
		BlockUtils.fillArea(167, 80, 150, 169, 80, 152, material);
	}	

}
