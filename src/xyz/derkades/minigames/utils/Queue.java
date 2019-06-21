package xyz.derkades.minigames.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.Minigames;

public class Queue {

	private static final List<Runnable> QUEUE = new ArrayList<>();

	public static void add(final Runnable runnable) {
		QUEUE.add(runnable);
	}

	public static void start() {
		new QueueProcessor().runTaskTimer(Minigames.getInstance(), 20, 2);
	}

	public static class QueueProcessor extends BukkitRunnable {

		@Override
		public void run() {
			if (QUEUE.size() > 0) {
				System.out.println("[debug] Processing task queue, " + QUEUE.size() + " entries left.");
				QUEUE.remove(0).run();
			}
		}

	}

}
