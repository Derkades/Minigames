package com.robinmc.minigames.task;

import org.bukkit.scheduler.BukkitRunnable;

import com.robinmc.minigames.Var;
import com.robinmc.minigames.games.SaveTheSnowman;

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
