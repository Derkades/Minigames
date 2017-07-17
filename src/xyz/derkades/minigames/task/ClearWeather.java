package xyz.derkades.minigames.task;

import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.SaveTheSnowman;

public class ClearWeather extends BukkitRunnable {

	@Override
	public void run() {
		boolean snowmanGameRunning = new SaveTheSnowman().isRunning();
		if (snowmanGameRunning){
			Var.WORLD.setStorm(true); //Snow for SaveTheSnowman game
		} else if (Var.WORLD.hasStorm()){
			Var.WORLD.setStorm(false);
		}
	}

}
