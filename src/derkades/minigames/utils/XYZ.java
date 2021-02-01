package derkades.minigames.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class XYZ {

	private final double x;
	private final double y;
	private final double z;

	public XYZ(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}

	public Location getLocation(final World world) {
		return new Location(world, this.x, this.y, this.z);
	}

}
