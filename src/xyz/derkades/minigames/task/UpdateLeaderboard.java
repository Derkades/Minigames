package xyz.derkades.minigames.task;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.Logger;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;

public class UpdateLeaderboard implements Runnable {
	
	@Override
	public void run() {
		final int lastGameNumber = Minigames.getInstance().getConfig().getInt("last-game-number");
		
		// Retrieve game info from files async
		Scheduler.async(() -> {
			final Map<UUID, Integer> winsByUuid = new HashMap<>();
			for (int i = lastGameNumber-100; i <= lastGameNumber; i++) {
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
			
			final Map<UUID, Integer> sorted = Utils.sortByValue(winsByUuid);
			
			Scheduler.run(() -> {
				final String[][] leaderboard = new String[3][12];
				leaderboard[0][0] = ChatColor.GREEN + "Username";
				leaderboard[1][0] = ChatColor.GREEN + "Recent wins";
				leaderboard[2][0] = ChatColor.GREEN + "Total points";

				int i = 1;
				for (final Map.Entry<UUID, Integer> wins : sorted.entrySet()) {
					final UUID uuid = wins.getKey();
					final int recentWins = wins.getValue();
					final int totalPoints = Minigames.getInstance().getConfig().getInt("points." + uuid, 0);
					final String name = Bukkit.getOfflinePlayer(uuid).getName();
					leaderboard[0][i] = ChatColor.WHITE + (name == null ? "???" : name);
					leaderboard[1][i] = ChatColor.WHITE + "" + recentWins;
					leaderboard[2][i] = ChatColor.WHITE + "" + totalPoints;
					i = i + 1;
					if (i > 12) {
						break;
					}
				}
				
				renderLeaderboard(leaderboard);
			});
		});
	}
	
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
	
	private void renderLeaderboard(final String[][] leaderboard) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				final Sign sign = (Sign) LEADERBOARD_SIGNS[i][j].getBlock().getState();
				for (int line = 0; line < 4; line++) {
					sign.setLine(line, leaderboard[i][j*4+line]);
				}
				sign.update();
			}
		}
//				final ChatColor color = ChatColor.WHITE;
//				final String nickname = array[i].getName();
				
//				sign.setLine(j, line);
//				sign.setLine(0, ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + (i + 1) + ChatColor.DARK_GRAY + "]");
//				sign.setLine(1, color + nickname);
//				sign.setLine(2, ChatColor.WHITE + "" + Points.getPoints(array[i]));
				
//			}
//		}
		

//		for (int i = 0; i < 9; i++) {
//			final ChatColor color = ChatColor.WHITE;
//			final String nickname = array[i].getName();
//			final Sign sign = (Sign) signLocations[i].getBlock().getState();
//			sign.setLine(0, ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + (i + 1) + ChatColor.DARK_GRAY + "]");
//			sign.setLine(1, color + nickname);
//			sign.setLine(2, ChatColor.WHITE + "" + Points.getPoints(array[i]));
//			sign.update();
//		}

		final int players = Bukkit.getOfflinePlayers().length;
//		final int totalPoints = map.values().stream().mapToInt(Integer::intValue).sum();
		final int gamesPlayed = Minigames.getInstance().getConfig().getInt("last-game-number");

		final Sign globalStats = (Sign) new Location(Var.LOBBY_WORLD, 221, 65, 280).getBlock().getState();
		globalStats.setLine(0, ChatColor.YELLOW + "Players");
		globalStats.setLine(1, ChatColor.WHITE + "" + players);
		globalStats.setLine(2, ChatColor.YELLOW + "Games played");
		globalStats.setLine(3, ChatColor.WHITE + "" + gamesPlayed);
		globalStats.update();
		
		final Sign globalStats2 = (Sign) new Location(Var.LOBBY_WORLD, 217, 65, 280).getBlock().getState();
		globalStats2.setLine(0, ChatColor.YELLOW + "Games");
		final int gameCount = Game.GAMES.length;
		final int mapCount = Arrays.stream(Game.GAMES).mapToInt(g -> g.getGameMaps().length).sum();
		globalStats2.setLine(1, String.format(ChatColor.WHITE + "%s (%s maps)", gameCount, mapCount));
		globalStats2.setLine(2, ChatColor.YELLOW + "Zombies killed");
		final int killCount = Minigames.getInstance().getConfig().getInt("zombie-kill-count", 0);
		globalStats2.setLine(3, ChatColor.WHITE + "" + killCount);
		globalStats2.update();
	}

}