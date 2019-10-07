package xyz.derkades.minigames.utils;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import xyz.derkades.minigames.Minigames;

public class Scheduler {

	public static BukkitTask delay(final long delay, final Runnable runnable) {
		return Bukkit.getScheduler().runTaskLater(Minigames.getInstance(), runnable, delay);
	}

	public static BukkitTask repeat(final long interval, final Runnable runnable) {
		return repeat(0, interval, runnable);
	}

	public static BukkitTask repeat(final long delay, final long interval, final Runnable runnable) {
		return Bukkit.getScheduler().runTaskTimer(Minigames.getInstance(), runnable, delay, interval);
	}

	public static BukkitTask run(final Runnable runnable) {
		return Bukkit.getScheduler().runTask(Minigames.getInstance(), runnable);
	}

	public static BukkitTask async(final Runnable runnable) {
		return Bukkit.getScheduler().runTaskAsynchronously(Minigames.getInstance(), runnable);
	}

}
