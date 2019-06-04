package xyz.derkades.minigames.games.breaktheblock;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;

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
		new Location(Var.WORLD, 147, 47, 400).getBlock().setType(Material.GOLD_BLOCK);
	}

	@Override
	public void onStart() {

	}

	@Override
	public void timer() {

	}

	@Override
	public Location getStartLocation() {
		return new Location(Var.WORLD, 131.5, 46, 400.5, -80, 0);
	}

}
