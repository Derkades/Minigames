package derkades.minigames.utils.queue;

import org.jetbrains.annotations.NotNull;

import derkades.minigames.utils.Scheduler;

public record Task(@NotNull Runnable runnable, boolean async) {

	public void run() {
		if (this.async) {
			Scheduler.async(this.runnable);
		} else {
			this.runnable.run();
		}
	}

}
