package derkades.minigames.utils.queue;

import java.util.LinkedList;
import java.util.Queue;

import org.jetbrains.annotations.NotNull;

import derkades.minigames.utils.Scheduler;

public class TaskQueue {

	private static final Queue<Task> QUEUE = new LinkedList<>();

	public static void add(@NotNull final Runnable runnable) {
		add(runnable, false);
	}

	public static void add(@NotNull final Runnable runnable, final boolean async) {
		QUEUE.add(new Task(runnable, async));
	}

	public static void start() {
		Scheduler.repeat(20, 1, () -> {
			if (!QUEUE.isEmpty()) {
//				Logger.debug("Processing task queue, " + QUEUE.size() + " entries left.");
				QUEUE.remove().run();
			}
		});
	}

	public static int size() {
		return QUEUE.size();
	}

}
