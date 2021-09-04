package derkades.minigames.games.dropper;

import org.bukkit.Location;
import org.bukkit.Material;

import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import xyz.derkades.derkutils.bukkit.BlockUtils;

class Redstone extends DropperMap {

	@Override
	public String getName() {
		return "Redstone";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.DROPPER_REDSTONE;
	}

	@Override
	public String getCredits() {
		return "RedstonerNor, Chaspyr";
	}

	@Override
	public String getIdentifier() {
		return "dropper_redstone";
	}

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	Location getLobbyLocation() {
		return new Location(this.getWorld(), 0.5, 85, 0.5, -90, 0);
	}

	@Override
	void openDoor() {
		BlockUtils.fillArea(this.getWorld(), 6, 84, -1, 4, 84, 1, Material.AIR);
		new Location(this.getWorld(), 132, 66, 141).getBlock().setType(Material.REDSTONE_BLOCK);
	}

	@Override
	void closeDoor() {
		BlockUtils.fillArea(this.getWorld(), 6, 84, -1, 4, 84, 1, Material.WHITE_STAINED_GLASS);
	}

}
