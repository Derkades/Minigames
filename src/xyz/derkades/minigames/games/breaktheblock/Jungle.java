package xyz.derkades.minigames.games.breaktheblock;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.worlds.GameWorld;

public class Jungle extends BreakTheBlockMap {

	@Override
	public void onPreStart() {
	}

	@Override
	public void onStart() {
		new Location(this.getWorld(), 38, 65, -21).getBlock().setType(Material.GOLD_BLOCK);

	}

	@Override
	public void timer() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			if (player.getBlockIn().getType() == Material.LAVA ) {
				player.teleport(this.getStartLocation());
				player.removeFire();
			}
		}
	}

	@Override
	public Location getStartLocation() {
		return new Location(this.getWorld(), 0.5, 65, 0.5, -123f, 14f);
	}

	@Override
	public String getName() {
		return "Jungle";
	}

	@Override
	public MapSize getSize() {
		return null;
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.BTB_JUNGLE;
	}

	@Override
	public String getCredits() {
		return "funlolxxl";
	}

}
