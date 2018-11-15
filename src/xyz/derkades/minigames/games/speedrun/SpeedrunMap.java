package xyz.derkades.minigames.games.speedrun;

import org.bukkit.Location;
import org.bukkit.Material;

public abstract class SpeedrunMap {
	
	public static final SpeedrunMap[] MAPS = {
		new Classic(),
	}
	
	public Material getFloorBlock() {
		return Material.STAINED_CLAY;
	}
	
	public short getFloorData() {
		return 14;
	}
	
	public Material getEndBlock() {
		return Material.STAINED_CLAY;
	}
	
	public short getEndData() {
		return 7;
	}
	
	public abstract Location getStartLocation();
	
	public abstract Location getSpectatorLocation();

}
