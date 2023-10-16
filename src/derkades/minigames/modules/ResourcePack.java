package derkades.minigames.modules;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.utils.PluginLoadEvent;
import derkades.minigames.utils.Scheduler;
import net.kyori.adventure.text.Component;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResourcePack extends Module {

	private static final MessageDigest SHA1_ENCODER;
	static {
		try {
			SHA1_ENCODER = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private static final String EMPTY_DOWNLOAD_URL = "https://downloads.rkslot.nl/empty.zip";
	private static final byte[] EMPTY_HASH = hexToBytes("6bf5ff711e75e780d2fa5e8ecfad977b6684e73f");
	private static final String DOWNLOAD_URL = "https://downloads.rkslot.nl/minigames.zip";
	private static final URI DOWNLOAD_URI = URI.create(DOWNLOAD_URL);

	private final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
	private final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(10))
			.executor(this.THREAD_POOL)
			.build();

	private byte[] hash = null;
	private static ResourcePack instance;

	public ResourcePack() {
		super();
		instance = this;
	}

	@EventHandler
	public void onLoad(final PluginLoadEvent event) {
		final FileConfiguration config = Minigames.getInstance().getConfig();
		if (config.isString("resource-pack-hash")) {
			try {
				//noinspection ConstantConditions
				this.hash = hexToBytes(config.getString("resource-pack-hash"));
			} catch (final Exception e) {
				Logger.warning("Unable to decode cached resource pack hash, forcing refresh");
				e.printStackTrace();
				this.refreshAsync();
			}
		} else {
			Logger.warning("Unable to load cached resource pack hash from config file, forcing refresh");
			this.refreshAsync();
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
				response = this.HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofByteArray());
			} catch (final IOException | InterruptedException e) {
				e.printStackTrace();
				return;
			}
			final byte[] pack = response.body();
			Logger.debug("Received %.2f MB.", pack.length / 1_000_000f);
			final byte[] hash = SHA1_ENCODER.digest(pack);
			Logger.debug("Pack SHA-1 hash: %s", bytesToHex(hash));
			if (Arrays.equals(this.hash, hash)) {
				Scheduler.run(() -> {
					Logger.debug("Resource pack hasn't changed");
				});
			} else {
				this.hash = hash;
				Scheduler.run(() -> {
					Logger.debug("Resource pack has changed, please rejoin to apply.");
					Minigames.getInstance().getConfig().set("resource-pack-hash", bytesToHex(this.hash));
					Minigames.getInstance().queueConfigSave();
//					Logger.debug("Sending new pack to all online players");
//					for (final Player player : Bukkit.getOnlinePlayers()) {
//						sendPack(player);
//					}
				});
			}
		});
	}

	private void sendPack(final Player player) {
		if (this.hash == null) {
			Logger.warning("Not sending resource pack to %s, hash is not known (yet?)", player.getName());
		}
		player.setResourcePack(DOWNLOAD_URL, this.hash, Component.text("Minigames resource pack"));
	}

	public static void sendEmptyPack(final Player player) {
		player.setResourcePack(EMPTY_DOWNLOAD_URL, EMPTY_HASH, Component.text("Empty pack resource pack"));
	}

	public static void sendResourcePack(final Player player) {
		instance.sendPack(player);
	}

	public static void refresh() {
		instance.refreshAsync();
	}

	private static final byte[] HEX_ARRAY = "0123456789abcdef".getBytes(StandardCharsets.US_ASCII);
	private static String bytesToHex(byte[] bytes) {
		byte[] hexChars = new byte[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars, StandardCharsets.UTF_8);
	}

	private static byte[] hexToBytes(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}

}
