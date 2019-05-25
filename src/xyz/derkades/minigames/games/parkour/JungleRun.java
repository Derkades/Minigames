package xyz.derkades.minigames.games.parkour;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.Utils;

public class JungleRun extends ParkourMap {

	@Override
	public String getName() {
		return "Jungle";
	}

	@Override
	public MapSize getSize() {
		return null;
	}

	@Override
	public Location getStartLocation() {
		return new Location(Var.WORLD, 282.5, 67, 196.5, -90, 0);
	}

	@Override
	public Location getSpectatorLocation() {
		return new Location(Var.WORLD, 296.5, 80, 204.5, 180, 0);
	}

	@Override
	public boolean hasFinished(Player player, Material blockType) {
		return blockType == Material.GOLD_BLOCK;
	}

	@Override
	public boolean hasDied(Player player, Material blockType) {
		return  blockType == Material.SOUL_SAND || // TODO Figure out if it's light or dark green carpet/wool/terracotta
				blockType == Material.GREEN_WOOL ||
				blockType == Material.LIME_WOOL ||
				blockType == Material.GREEN_CARPET ||
				blockType == Material.LIME_CARPET ||
				blockType == Material.GREEN_TERRACOTTA;
	}

	@Override
	public boolean spectatorFreeFlight() {
		return true;
	}

	@Override
	public int getDuration() {
		return 30;
	}
	
	@Override
	public void onPlayerFinish(Player player) {
		Utils.particle(Particle.FLAME, 327.5, 72, 196.5, 0.1, 1000, 0, 2, 2);
	}

}
