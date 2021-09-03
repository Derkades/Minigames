package derkades.minigames.modules;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.utils.PluginLoadEvent;
import derkades.minigames.utils.PluginUnloadEvent;
import derkades.minigames.utils.Scheduler;
import xyz.derkades.derkutils.NumberUtils;

public class ResourcePack extends Module {

	private static final String EMPTY_DOWNLOAD_URL = "http://vps3.derkad.es:12345/empty.zip";
	private static final String DOWNLOAD_URL = "http://vps3.derkad.es:12345/minigames.zip";
	private static final URI DOWNLOAD_URI = URI.create(DOWNLOAD_URL);

	private final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
	private final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(10))
			.executor(this.THREAD_POOL)
			.build();

	private byte[] hash;
	private static ResourcePack instance;

	public ResourcePack() {
		super();
		instance = this;
	}

	@EventHandler
	public void onLoad(final PluginLoadEvent event) {
		final FileConfiguration config = Minigames.getInstance().getConfig();
		if (config.isString("previous-pack-hash")) {
			try {
				this.hash = Hex.decodeHex(config.getString("previous-pack-hash"));
			} catch (final DecoderException e) {
				Logger.warning("Unable to decode resource pack hash");
				e.printStackTrace();
			}
		} else {
			Logger.warning("Unable to load previous resource pack hash from config file");
		}

		refreshAsync();
	}

	@EventHandler
	public void onUnload(final PluginUnloadEvent event) {
		if (this.hash != null) {
			Minigames.getInstance().getConfig().set("previous-pack-hash", Hex.encodeHexString(this.hash));
			Minigames.getInstance().saveConfig();
		}
	}

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		sendPack(event.getPlayer());
	}

	private void refreshAsync() {
		Scheduler.async(() -> {
			Logger.debug("Downloading resource pack from %s...", DOWNLOAD_URL);
			final HttpRequest request = HttpRequest.newBuilder(DOWNLOAD_URI).GET().build();
			HttpResponse<byte[]> response;
			try {
				response = this.HTTP_CLIENT.send(request, BodyHandlers.ofByteArray());
			} catch (final IOException e) {
				e.printStackTrace();
				return;
			} catch (final InterruptedException e) {
				e.printStackTrace();
				return;
			}
			final byte[] pack = response.body();
			Logger.debug("Received %s MB.", NumberUtils.roundApprox(pack.length / 1_000_000f, 2));
			final byte[] hash = DigestUtils.sha1(pack);
			Logger.debug("Pack SHA-1 hash: %s", Hex.encodeHexString(hash));
			if (Arrays.equals(this.hash, hash)) {
				Logger.debug("Resource pack hasn't changed");
				return;
			} else {
				this.hash = hash;
				Logger.debug("Sending new pack to all online players");
				Scheduler.run(() -> {
					for (final Player player : Bukkit.getOnlinePlayers()) {
						sendPack(player);
					}
				});
			}
		});
	}

	private void sendPack(final Player player) {
		if (this.hash == null) {
			Logger.warning("Not sending resource pack to %s, hash is not known (yet?)", player.getName());
		}
		player.setResourcePack(DOWNLOAD_URL, this.hash);
	}

	public static void sendEmptyPack(final Player player) {
		player.setResourcePack(EMPTY_DOWNLOAD_URL);
	}

	public static void sendResourcePack(final Player player) {
		instance.sendPack(player);
	}

	public static void refresh() {
		instance.refreshAsync();
	}

}
