package xyz.derkades.minigames.utils;

import org.bukkit.Bukkit;

import xyz.derkades.minigames.Minigames;

public class Scheduler {
	
	public static void runTaskLater(long delay, Runnable runnable){
		Bukkit.getScheduler().scheduleSyncDelayedTask(Minigames.getInstance(), runnable, delay);
	}
	
	public static void oneTickDelay(Runnable runnable){
		runTaskLater(1, runnable);
	}
	
	public static void runAsyncRepeatingTask(long start, long delay, Runnable runnable){
		Bukkit.getScheduler().runTaskTimerAsynchronously(Minigames.getInstance(), runnable, start, delay);
	}
	
	public static void runAsyncRepeatingTask(long delay, Runnable runnable){
		Bukkit.getScheduler().runTaskTimerAsynchronously(Minigames.getInstance(), runnable, 1, delay);
	}
	
	public static void runSyncRepeatingTask(long start, long delay, Runnable runnable){
		Bukkit.getScheduler().runTaskTimer(Minigames.getInstance(), runnable, start, delay);
	}
	
	public static void runSyncRepeatingTask(long delay, Runnable runnable){
		Bukkit.getScheduler().runTaskTimer(Minigames.getInstance(), runnable, 0, delay);
	}
	
}
