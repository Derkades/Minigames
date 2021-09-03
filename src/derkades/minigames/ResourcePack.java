package derkades.minigames;

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

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import derkades.minigames.utils.Scheduler;
import xyz.derkades.derkutils.NumberUtils;

public class ResourcePack {

	private static final URI DOWNLOAD_URL = URI.create("http://vps3.derkad.es:12345/minigames.zip");

	private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(10))
			.executor(THREAD_POOL)
			.build();

	private static byte[] hash;

	public static void refreshAsync() {
		Scheduler.async(() -> {
			Logger.debug("Downloading resource pack from %s...", DOWNLOAD_URL);
			final HttpRequest request = HttpRequest.newBuilder(DOWNLOAD_URL).GET().build();
			HttpResponse<byte[]> response;
			try {
				response = HTTP_CLIENT.send(request, BodyHandlers.ofByteArray());
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
			if (Arrays.equals(ResourcePack.hash, hash)) {
				Logger.debug("Resource pack hasn't changed");
				return;
			} else {
				ResourcePack.hash = hash;
				Logger.debug("Sending new pack to all online players");
				Scheduler.run(() -> {
					for (final Player player : Bukkit.getOnlinePlayers()) {
						sendPack(player);
					}
				});
			}
		});
	}

	public static void sendPack(final Player player) {
		if (hash == null) {
			Logger.warning("Not sending resource pack to %s, hash is not known (yet?)", player.getName());
		}
		player.setResourcePack(DOWNLOAD_URL.toString(), hash);
	}

	public static void sendEmptyPack(final Player player) {
		player.setResourcePack("http://vps3.derkad.es:12345/empty.zip");
	}

}
