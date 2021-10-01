package derkades.minigames.utils;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import derkades.minigames.Minigames;

@SuppressWarnings("UnusedReturnValue")
public class Scheduler {

	@SuppressWarnings("UnusedReturnValue")
	public static BukkitTask delay(final long delay, @NotNull final Runnable runnable) {
		return Bukkit.getScheduler().runTaskLater(Minigames.getInstance(), runnable, delay);
	}

	public static BukkitTask repeat(final long interval, @NotNull final Runnable runnable) {
		return repeat(0, interval, runnable);
	}

	public static BukkitTask repeat(final long delay, final long interval, @NotNull final Runnable runnable) {
		return Bukkit.getScheduler().runTaskTimer(Minigames.getInstance(), runnable, delay, interval);
	}

	public static BukkitTask run(@NotNull final Runnable runnable) {
		return Bukkit.getScheduler().runTask(Minigames.getInstance(), runnable);
	}

	public static BukkitTask async(@NotNull final Runnable runnable) {
		return Bukkit.getScheduler().runTaskAsynchronously(Minigames.getInstance(), runnable);
	}

}
