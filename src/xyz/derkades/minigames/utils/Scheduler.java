package xyz.derkades.minigames.utils;

import org.bukkit.Bukkit;

import xyz.derkades.minigames.Main;

public class Scheduler {
	
	public static void runTaskLater(long delay, Runnable runnable){
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), runnable, delay);
	}
	
	public static void oneTickDelay(Runnable runnable){
		runTaskLater(1, runnable);
	}
	
	public static void runAsyncRepeatingTask(long start, long delay, Runnable runnable){
		Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), runnable, start, delay);
	}
	
	public static void runAsyncRepeatingTask(long delay, Runnable runnable){
		Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), runnable, 1, delay);
	}
	
	public static void runSyncRepeatingTask(long start, long delay, Runnable runnable){
		Bukkit.getScheduler().runTaskTimer(Main.getInstance(), runnable, start, delay);
	}
	
	public static void runSyncRepeatingTask(long delay, Runnable runnable){
		Bukkit.getScheduler().runTaskTimer(Main.getInstance(), runnable, 0, delay);
	}
	
}
