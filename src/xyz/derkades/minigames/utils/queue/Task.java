package xyz.derkades.minigames.utils.queue;

import xyz.derkades.minigames.utils.Scheduler;

public class Task implements Comparable<Task> {
	
	private final Runnable runnable;
	private final TaskPriority priority;
	private final boolean async;
	
	public Task(final Runnable runnable, final TaskPriority priority, final boolean async) {
		this.runnable = runnable;
		this.priority = priority;
		this.async = async;
	}
	
	public void run() {
		if (this.async) {
			Scheduler.async(this.runnable);
		} else {
			this.runnable.run();
		}
	}

	@Override
	public int compareTo(final Task item) {
		return this.priority.compareTo(item.priority);
	}
	
}
