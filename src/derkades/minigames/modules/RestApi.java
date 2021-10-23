package derkades.minigames.modules;

import com.google.gson.stream.JsonWriter;
import derkades.minigames.GameState;
import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.PluginLoadEvent;
import derkades.minigames.utils.PluginUnloadEvent;
import org.bukkit.event.EventHandler;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.server.ServerConfiguration;

import java.io.IOException;

public class RestApi extends Module {

	private static final int PORT = 26495;

	private final HttpServer server;

	public RestApi() {
		this.server = new HttpServer();
		NetworkListener listener = new NetworkListener("Listener", "0.0.0.0", PORT);
		server.addListener(listener);
		ServerConfiguration config = server.getServerConfiguration();
		config.addHttpHandler(new HttpHandler() {
			@Override
			public void service(Request request, Response response) throws Exception {
				response.setContentType("application/json");
				try (JsonWriter writer = new JsonWriter(response.getWriter())) {
					writer.beginObject();

					writer.name("online_players");
					writer.beginArray();
					for (MPlayer player : Minigames.getOnlinePlayers()) {
						writer.beginObject();
						writer.name("uuid").value(player.getUniqueId().toString());
						writer.name("name").value(player.getOriginalName());
						writer.name("has_debug_perm").value(player.bukkit().hasPermission("minigames.debug"));
						writer.name("points").value(player.getPoints());
						writer.endObject();
					}
					writer.endArray();
					
					writer.name("game_state");
					writer.beginObject();
					GameState state = GameState.getCurrentState();
					writer.name("name").value(state.name());
					writer.name("in_game").value(state.isInGame());
					writer.name("running").value(state.gameIsRunning());
					writer.endObject();

					writer.endObject();
				}
			}
		});
	}

	@EventHandler
	public void onLoad(PluginLoadEvent event) {
		Logger.debug("Starting webserver");
		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onUnload(PluginUnloadEvent event) {
		Logger.debug("Stopping webserver");
		server.shutdownNow();
	}

}
