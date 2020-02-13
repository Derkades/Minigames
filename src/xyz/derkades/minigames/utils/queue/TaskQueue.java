package xyz.derkades.minigames.utils.queue;

import java.util.PriorityQueue;

import xyz.derkades.minigames.utils.Scheduler;

public class TaskQueue {
	
	private static final PriorityQueue<Task> QUEUE = new PriorityQueue<>();

	public static void add(final Runnable runnable, final TaskPriority priority, final boolean async) {
		QUEUE.add(new Task(runnable, priority, async));
	}
	
	public static void add(final Runnable runnable, final TaskPriority priority) {
		add(runnable, priority, false);
	}
	
	public static void add(final Runnable runnable) {
		add(runnable, TaskPriority.NORMAL);
	}
	
	public static void add(final Runnable runnable, final boolean async) {
		add(runnable, TaskPriority.NORMAL, async);
	}

	public static void start() {
		Scheduler.repeat(20, 2, () -> {
			if (!QUEUE.isEmpty()) {
				System.out.println("[debug] Processing task queue, " + QUEUE.size() + " entries left.");
				QUEUE.remove().run();
			}
		});
	}
	
}
