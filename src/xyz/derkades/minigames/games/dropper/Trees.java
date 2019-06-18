package xyz.derkades.minigames.games.dropper;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.BlockUtils;
import xyz.derkades.minigames.worlds.GameWorld;

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
		this.fillDoor(Material.AIR);
	}

	@Override
	public void closeDoor() {
		this.fillDoor(Material.GREEN_STAINED_GLASS);
	}

	private void fillDoor(final Material material) {
		BlockUtils.fillArea(167, 80, 150, 169, 80, 152, material);
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
