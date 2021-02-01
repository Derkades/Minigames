package derkades.minigames.task;

import org.bukkit.scheduler.BukkitRunnable;

import derkades.minigames.Minigames;
import derkades.minigames.utils.MPlayer;

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
