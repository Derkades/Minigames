package xyz.derkades.minigames.games.pointcontrol;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.derkutils.bukkit.LocationUtils;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.worlds.GameWorld;

public class Prototype extends PointControlMap {

	@Override
	public Size getSize() {
		return Size.LARGE;
	}

	@Override
	public Location getBlueSpawnLocation() {
		return new Location(Var.WORLD, 437.5, 69, 374.5);
	}

	@Override
	public Location getRedSpawnLocation() {
		return new Location(Var.WORLD, 470.5, 69, 427.5);
	}

	@Override
	public Location[] getControlPointLocations() {
		return new Location[] {
				new Location(Var.WORLD, 403, 68, 372),
				new Location(Var.WORLD, 405, 72, 403),
				new Location(Var.WORLD, 499, 72, 395),
				new Location(Var.WORLD, 501, 68, 426),
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
//		Logger.debug("Setting control point status (%s, %s, %s) to %s", location.getX(), location.getY(), location.getZ(), status);
		
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
		return null;
	}

	@Override
	public String getCredits() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return "pointcontrol_prototype";
	}

}
