package derkades.minigames;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import derkades.minigames.games.Games;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpdateSigns {

	private static final int LEADERBOARD_LAST_GAME_COUNT = 350;

	private static final Location[][] LEADERBOARD_SIGNS = new Location[3][3];

	static {
		LEADERBOARD_SIGNS[0][0] = new Location(Var.LOBBY_WORLD, 225, 66, 273);
		LEADERBOARD_SIGNS[0][1] = new Location(Var.LOBBY_WORLD, 225, 65, 273);
		LEADERBOARD_SIGNS[0][2] = new Location(Var.LOBBY_WORLD, 225, 64, 273);
		LEADERBOARD_SIGNS[1][0] = new Location(Var.LOBBY_WORLD, 225, 66, 274);
		LEADERBOARD_SIGNS[1][1] = new Location(Var.LOBBY_WORLD, 225, 65, 274);
		LEADERBOARD_SIGNS[1][2] = new Location(Var.LOBBY_WORLD, 225, 64, 274);
		LEADERBOARD_SIGNS[2][0] = new Location(Var.LOBBY_WORLD, 225, 66, 275);
		LEADERBOARD_SIGNS[2][1] = new Location(Var.LOBBY_WORLD, 225, 65, 275);
		LEADERBOARD_SIGNS[2][2] = new Location(Var.LOBBY_WORLD, 225, 64, 275);
	}

	public static void updateLeaderboard() {
		final int lastGameNumber = Minigames.getInstance().getConfig().getInt("last-game-number");

		// Retrieve game info from files async
		Scheduler.async(() -> {
			final long readStart = System.currentTimeMillis();
			final Map<UUID, Integer> winsByUuid = new HashMap<>();
			for (int i = lastGameNumber-LEADERBOARD_LAST_GAME_COUNT; i <= lastGameNumber; i++) {
				final File file = new File("game_results", i + ".json");
				try (final Reader reader = new FileReader(file)) {
					final JsonObject json = (JsonObject) JsonParser.parseReader(reader);
					final JsonArray winners = json.getAsJsonArray("winners");
					for (final JsonElement winner : winners) {
						final UUID uuid = UUID.fromString(winner.getAsJsonObject().get("uuid").getAsString());
						winsByUuid.compute(uuid, (k, v) -> v == null ? 1 : v + 1);
					}
				} catch (final IOException e) {
					Logger.warning("Failed to read game result file %s", file.getAbsolutePath());
					e.printStackTrace();
				}
			}
			final long readEndSortStart = System.currentTimeMillis();

			final Map<UUID, Integer> sorted = Utils.sortByValue(winsByUuid);
			final long sortEnd = System.currentTimeMillis();

			Scheduler.run(() -> {
				final long fillStart = System.currentTimeMillis();
				final Component[][] leaderboard = new Component[3][12];
				leaderboard[0][0] = Component.text("Username", NamedTextColor.YELLOW);
				leaderboard[1][0] = Component.text("Recent wins", NamedTextColor.YELLOW);
				leaderboard[2][0] = Component.text("Total points", NamedTextColor.YELLOW);

				int i = 1;
				for (final Map.Entry<UUID, Integer> wins : sorted.entrySet()) {
					final UUID uuid = wins.getKey();
					final int recentWins = wins.getValue();
					final int totalPoints = Minigames.getInstance().getConfig().getInt("points." + uuid, 0);
					final String name = Bukkit.getOfflinePlayer(uuid).getName();
					leaderboard[0][i] = Component.text(name == null ? "?" : name, NamedTextColor.WHITE);
					leaderboard[1][i] = Component.text(recentWins, NamedTextColor.WHITE);
					leaderboard[2][i] = Component.text(totalPoints, NamedTextColor.WHITE);
					i = i + 1;
					if (i > 12) {
						break;
					}
				}

//				leaderboard[0][11] = Component.text('\ue004', NamedTextColor.WHITE);
//				leaderboard[2][11] = Component.text('\ue005', NamedTextColor.WHITE);

				final long fillEndRenderStart = System.currentTimeMillis();

				renderLeaderboard(leaderboard);

				final long renderEnd = System.currentTimeMillis();

				Logger.debug("Updated leaderboard - %sms(A) read, %sms(A) sort, %sms(S) fill, %sms(S) render",
						readEndSortStart - readStart,
						sortEnd - readEndSortStart,
						fillEndRenderStart - fillStart,
						renderEnd - fillEndRenderStart);
			});
		});
	}

	private static void renderLeaderboard(final Component[][] components) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				final Sign sign = (Sign) LEADERBOARD_SIGNS[i][j].getBlock().getState();
				for (int line = 0; line < 4; line++) {
					final Component component = components[i][j*4+line];
					sign.line(line, component == null ? Component.empty() : component);
				}
				sign.update();
			}
		}
	}

	public static void updateGlobalStats() {
		final int uniquePlayerCount = Bukkit.getOfflinePlayers().length;
		final int gamesPlayed = Minigames.getInstance().getConfig().getInt("last-game-number");

		final Sign globalStats = (Sign) new Location(Var.LOBBY_WORLD, 221, 65, 280).getBlock().getState();
		globalStats.line(0, Component.text("Players", NamedTextColor.YELLOW));
		globalStats.line(1, Component.text(uniquePlayerCount, NamedTextColor.WHITE));
		globalStats.line(2, Component.text("Games played", NamedTextColor.YELLOW));
		globalStats.line(3, Component.text(gamesPlayed, NamedTextColor.WHITE));
		globalStats.update();

		final Sign globalStats2 = (Sign) new Location(Var.LOBBY_WORLD, 217, 65, 280).getBlock().getState();
		globalStats2.line(0, Component.text("Games", NamedTextColor.YELLOW));
		final int gameCount = Games.GAMES.length;
		final int mapCount = Arrays.stream(Games.GAMES).mapToInt(g -> g.getGameMaps().length).sum();
		globalStats2.line(1, Component.text(gameCount + " (" + mapCount + " maps)", NamedTextColor.WHITE));
		globalStats2.line(2, Component.text("Zombies killed", NamedTextColor.YELLOW));
		final int killCount = Minigames.getInstance().getConfig().getInt("zombie-kill-count", 0);
		globalStats2.line(3, Component.text(killCount, NamedTextColor.WHITE));
		globalStats2.update();
	}

}
