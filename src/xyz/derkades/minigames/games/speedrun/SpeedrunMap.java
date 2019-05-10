package xyz.derkades.minigames.games.speedrun;

import org.bukkit.Location;
import org.bukkit.Material;

public abstract class SpeedrunMap {
	
	public static final SpeedrunMap[] MAPS = {
		new Backwards(),
		new Classic(),
		new Construction(),
		//new Trees(),
	};
	
	public Material getFloorBlock() {
		return Material.RED_TERRACOTTA;
	}
	
	public Material getEndBlock() {
		return Material.GRAY_TERRACOTTA;
	}
	
	public abstract Location getStartLocation();
	
	public abstract Location getSpectatorLocation();

}
