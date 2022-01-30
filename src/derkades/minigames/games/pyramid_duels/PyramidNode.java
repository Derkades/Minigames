package derkades.minigames.games.pyramid_duels;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class PyramidNode {

	private Location centerLocation;
	private int wallDistanceFromCenter;
	private PyramidNode @Nullable[] children;
	private BiConsumer<Boolean, Location> gateReplacer;

	public PyramidNode(Location centerLocation, int wallDistanceFromCenter, PyramidNode @Nullable[] children, BiConsumer<Boolean, Location> gateReplacer) {
		this.centerLocation = centerLocation;
		this.wallDistanceFromCenter = wallDistanceFromCenter;
		this.children = children;
		this.gateReplacer = gateReplacer;
	}

	public Location getCenterLocation() {
		return this.centerLocation;
	}

	public PyramidNode @Nullable[] getChildren() {
		return this.children;
	}

	public boolean isInNode(@NotNull Location location) {
		return location.getX() < centerLocation.getX() + wallDistanceFromCenter &&
				location.getX() > centerLocation.getX() - wallDistanceFromCenter &&
				location.getZ() < centerLocation.getZ() + wallDistanceFromCenter &&
				location.getZ() > centerLocation.getZ() - wallDistanceFromCenter;
	}

	public void openGates() {
		this.gateReplacer.accept(true, centerLocation);
	}

	public void closeGates() {
		this.gateReplacer.accept(true, centerLocation);
	}

}
