package xyz.derkades.minigames.games.dropper;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.derkutils.bukkit.BlockUtils;
import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.worlds.GameWorld;

public class Trees extends DropperMap {

	@Override
	public String getName() {
		return "Trees";
	}

	@Override
	public Location getLobbyLocation() {
		return new Location(this.getWorld(), -4.5, 82, -6.5, 90, 0);
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
		BlockUtils.fillArea(this.getWorld(), -9, 80, -6, -11, 80, -8, material);
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.DROPPER_TREES;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return "dropper_trees";
	}

}
