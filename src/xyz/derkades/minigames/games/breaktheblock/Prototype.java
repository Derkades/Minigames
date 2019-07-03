package xyz.derkades.minigames.games.breaktheblock;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.worlds.GameWorld;

public class Prototype extends BreakTheBlockMap {

	@Override
	public String getName() {
		return "Prototype";
	}

	@Override
	public MapSize getSize() {
		return null;
	}

	@Override
	public void onPreStart() {
		new Location(this.getWorld(), 16, 66, 0).getBlock().setType(Material.GOLD_BLOCK);
	}

	@Override
	public Location getStartLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5, -90f, 0f);
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.BTB_PROTOTYPE;
	}

	@Override
	public String getCredits() {
		return null;
	}

}
