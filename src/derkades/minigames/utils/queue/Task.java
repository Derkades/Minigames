package derkades.minigames.utils.queue;

import org.jetbrains.annotations.NotNull;

import derkades.minigames.utils.Scheduler;

public class Task {

	@NotNull
	private final Runnable runnable;
	private final boolean async;

	public Task(@NotNull final Runnable runnable, final boolean async) {
		this.runnable = runnable;
		this.async = async;
	}

	public void run() {
		if (this.async) {
			Scheduler.async(this.runnable);
		} else {
			this.runnable.run();
		}
	}

}
