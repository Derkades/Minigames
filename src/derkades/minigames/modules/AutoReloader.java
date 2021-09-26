package derkades.minigames.modules;

import derkades.minigames.Minigames;
import derkades.minigames.utils.Scheduler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.io.File;

@SuppressWarnings("ALL")
@Deprecated
public class AutoReloader {

	private File file;
	private final long lastModified;

	public AutoReloader() {
		this.file = null;

		for (final File file : Minigames.getInstance().getDataFolder().getParentFile().listFiles()) {
			if (!file.isFile()) {
				continue;
			}

			if (file.getName().toLowerCase().contains(Minigames.getInstance().getName().toLowerCase())) {
				this.file = file;
			}
		}

		if (this.file == null) {
			throw new RuntimeException("Can't find plugin jar file");
		}

		this.lastModified = this.file.lastModified();

		Scheduler.repeat(2*20, 20, () -> Scheduler.async(() -> {
			if (this.file.exists() && this.file.lastModified() > this.lastModified) {
				Bukkit.broadcast(Component.text("Plugin changed, reloading!"));
				Scheduler.run(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rl confirm"));
			}
		}));
	}

}
