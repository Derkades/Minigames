package xyz.derkades.minigames.games.pointcontrol;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.derkutils.bukkit.LocationUtils;
import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.worlds.GameWorld;

public class Prototype extends ControlPointsMap {

	@Override
	public Size getSize() {
		return Size.LARGE;
	}

	@Override
	public Location getBlueSpawnLocation() {
		return new Location(this.getWorld(), -15.6, 63, -25.5);
	}

	@Override
	public Location getRedSpawnLocation() {
		return new Location(this.getWorld(), 19.5, 63, 27.5);
	}

	@Override
	public Location[] getControlPointLocations() {
		return new Location[] {
				new Location(this.getWorld(), -49, 62, -28), // blue   1
				new Location(this.getWorld(), -48, 66,   3), // blue   2
				new Location(this.getWorld(),  -1, 76,  -1), // middle
				new Location(this.getWorld(),  47, 66,  -5), // red    2
				new Location(this.getWorld(),  49, 62,  26), // red    1
		};
	}

	@Override
	public boolean isOnControlPoint(final Location location, final MPlayer player) {
		final Location min = location;
		final Location max = location.clone().add(3, 5, 3);
		return LocationUtils.isIn2dBounds(player.getLocation(), min, max);
	}

	@Override
	public void setControlPointStatus(final Location location, final ControlStatus status) {
		Material newMaterial;
		if (status == ControlStatus.BLUE) {
			newMaterial = Material.BLUE_CONCRETE;
		} else if (status == ControlStatus.RED) {
			newMaterial = Material.RED_CONCRETE;
		} else {
			newMaterial = Material.GRAY_CONCRETE;
		}
		
		// Corners
		location             		 .getBlock().setType(newMaterial);
		location.clone().add(3, 0, 0).getBlock().setType(newMaterial);
		location.clone().add(0, 0, 3).getBlock().setType(newMaterial);
		location.clone().add(3, 0, 3).getBlock().setType(newMaterial);
		
		// Middle
		location.clone().add(1, 0, 1).getBlock().setType(newMaterial);
		location.clone().add(1, 0, 2).getBlock().setType(newMaterial);
		location.clone().add(2, 0, 1).getBlock().setType(newMaterial);
		location.clone().add(2, 0, 2).getBlock().setType(newMaterial);
	}

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.CONTROL_PROTOTYPE;
	}

	@Override
	public String getCredits() {
		return "PeeperSleeper";
	}

	@Override
	public String getIdentifier() {
		return "controlpoints_prototype";
	}

}
