package xyz.derkades.minigames.games.breaktheblock;

import org.bukkit.Location;
import org.bukkit.Material;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.worlds.GameWorld;

public class Jungle extends BreakTheBlockMap {

	@Override
	public void onPreStart() {
		new Location(this.getWorld(), 38, 65, -21).getBlock().setType(Material.GOLD_BLOCK);
	}

	@Override
	public void onTimer(final int secondsLeft) {
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
	public GameWorld getGameWorld() {
		return GameWorld.BTB_JUNGLE;
	}

	@Override
	public String getCredits() {
		return "funlolxxl";
	}

	@Override
	public Size getSize() {
		return null;
	}

	@Override
	public String getIdentifier() {
		return "breaktheblock_jungle";
	}

}
