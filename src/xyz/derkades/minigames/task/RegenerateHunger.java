package xyz.derkades.minigames.task;

import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.utils.MPlayer;

public class RegenerateHunger extends BukkitRunnable {

	@Override
	public void run() {
		for (final MPlayer player : Minigames.getOnlinePlayers()){
			if (player.getDisableHunger()) {
				player.bukkit().setFoodLevel(22);
			}
		}
	}

}
