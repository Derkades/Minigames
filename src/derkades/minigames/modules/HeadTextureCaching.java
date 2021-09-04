package derkades.minigames.modules;

import java.io.IOException;
import java.nio.file.Path;

import org.bukkit.event.EventHandler;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.utils.PluginLoadEvent;
import derkades.minigames.utils.PluginUnloadEvent;
import derkades.minigames.utils.Scheduler;
import xyz.derkades.derkutils.bukkit.HeadTextures;

public class HeadTextureCaching extends Module {

	private static final Path PATH = Minigames.getInstance().getDataFolder().toPath().resolve("head-texture-cache.json");

	@EventHandler
	public void onLoad(final PluginLoadEvent event) {
		Scheduler.async(() -> {
			try {
				final int amount = HeadTextures.readFileToCache(PATH);
				Logger.debug("Read %s head textures from cache file", amount);
			} catch (final IOException e) {
				e.printStackTrace();
				Logger.warning("Error reading head texture cache file");
			}
		});
	}

	@EventHandler
	public void onUnload(final PluginUnloadEvent event) {
		try {
			HeadTextures.saveCacheToFile(PATH);
			Logger.debug("Written head texture cache to file");
		} catch (final IOException e) {
			e.printStackTrace();
			Logger.warning("Error saving head texture cache to file");
		}
	}

}
