package xyz.derkades.minigames.games.dropper;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.utils.BlockUtils;

public class Redstone extends DropperMap {

	@Override
	public String getName() {
		return "Redstone";
	}

	@Override
	public Location getLobbyLocation() {
		return new Location(Var.WORLD, 116.5, 82.0, 130.5, -90, 0);
	}

	@Override
	public void openDoor() {
		BlockUtils.fillArea(121, 81, 129, 123, 81, 131, Material.AIR);
		new Location(Var.WORLD, 132, 66, 141).getBlock().setType(Material.REDSTONE_BLOCK);
	}

	@Override
	public void closeDoor() {
		BlockUtils.fillArea(121, 81, 129, 123, 81, 131, Material.WHITE_STAINED_GLASS);
	}

}
