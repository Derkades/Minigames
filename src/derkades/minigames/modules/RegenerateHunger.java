package derkades.minigames.modules;

import org.bukkit.GameMode;

import derkades.minigames.Minigames;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Scheduler;

public class RegenerateHunger extends Module implements Runnable {

	public RegenerateHunger() {
		Scheduler.repeat(40, this);
	}

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
