package xyz.derkades.minigames;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.derkades.minigames.utils.Scheduler;

public class AutoReloader {
	
	private File file;
	private final long lastModified;
	
	AutoReloader(final JavaPlugin plugin) {
		this.file = null;
		
		for (final File file : plugin.getDataFolder().getParentFile().listFiles()) {
			if (!file.isFile()) {
				continue;
			}
			
			if (file.getName().toLowerCase().contains(plugin.getName().toLowerCase())) {
				this.file = file;
			}
		}
		
		if (this.file == null) {
			throw new RuntimeException("Can't find plugin jar file");
		}
		
		this.lastModified = this.file.lastModified();
		
		Scheduler.repeat(2*20, 50, () -> {
			Scheduler.async(() -> {
				if (this.file.exists()) {
					if (this.file.lastModified() > this.lastModified) {
						Bukkit.broadcastMessage("Plugin change detected, reloading in 2 seconds!");
						Scheduler.delay(40, () -> {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rl confirm");
						});
					}
				}
			});
		});
	}

}
