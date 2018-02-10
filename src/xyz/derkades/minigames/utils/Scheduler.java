package xyz.derkades.minigames.utils;

import org.bukkit.Bukkit;

import xyz.derkades.minigames.Minigames;

public class Scheduler {
	
	public static void delay(long delay, Runnable runnable) {
		Bukkit.getScheduler().runTaskLater(Minigames.getInstance(), runnable, delay);
	}
	
	public static void repeat(long interval, Runnable runnable) {
		repeat(0, interval, runnable);
	}
	
	public static void repeat(long delay, long interval, Runnable runnable) {
		Bukkit.getScheduler().runTaskTimer(Minigames.getInstance(), runnable, delay, interval);
	}
	
	public static void run(Runnable runnable) {
		Bukkit.getScheduler().runTask(Minigames.getInstance(), runnable);
	}
	
	public static void async(Runnable runnable) {
		Bukkit.getScheduler().runTaskAsynchronously(Minigames.getInstance(), runnable);
	}
	
	@Deprecated
	public static void runTaskLater(long delay, Runnable runnable){
		Bukkit.getScheduler().scheduleSyncDelayedTask(Minigames.getInstance(), runnable, delay);
	}
	
	@Deprecated
	public static void oneTickDelay(Runnable runnable){
		runTaskLater(1, runnable);
	}
	
	@Deprecated
	public static void runAsyncRepeatingTask(long start, long delay, Runnable runnable){
		Bukkit.getScheduler().runTaskTimerAsynchronously(Minigames.getInstance(), runnable, start, delay);
	}
	
	@Deprecated
	public static void runAsyncRepeatingTask(long delay, Runnable runnable){
		Bukkit.getScheduler().runTaskTimerAsynchronously(Minigames.getInstance(), runnable, 1, delay);
	}
	
	@Deprecated
	public static void runSyncRepeatingTask(long start, long delay, Runnable runnable){
		Bukkit.getScheduler().runTaskTimer(Minigames.getInstance(), runnable, start, delay);
	}
	
	@Deprecated
	public static void runSyncRepeatingTask(long delay, Runnable runnable){
		Bukkit.getScheduler().runTaskTimer(Minigames.getInstance(), runnable, 0, delay);
	}
	
}
