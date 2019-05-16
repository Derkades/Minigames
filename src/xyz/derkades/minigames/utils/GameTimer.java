package xyz.derkades.minigames.utils;

import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.Minigames;

public abstract class GameTimer extends BukkitRunnable {
	
	public GameTimer(){
		super.runTaskTimer(Minigames.getInstance(), 20, 20);
	}

}
