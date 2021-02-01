package derkades.minigames.task;

import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

import derkades.minigames.Minigames;
import derkades.minigames.utils.MPlayer;

public class RegenerateHunger extends BukkitRunnable {

	@Override
	public void run() {
		for (final MPlayer player : Minigames.getOnlinePlayers()){
			if (player.getGameMode() == GameMode.ADVENTURE && player.getDisableHunger()) {
				player.bukkit().setFoodLevel(20);
				player.bukkit().setSaturation(20);
			}
		}
	}

}
