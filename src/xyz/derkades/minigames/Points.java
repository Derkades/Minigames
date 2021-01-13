package xyz.derkades.minigames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.games.Game;
import xyz.derkades.minigames.utils.Utils;

public class Points {

	public static void setPoints(final OfflinePlayer player, final int points){
		Minigames.getInstance().getConfig().set("points." + player.getUniqueId(), points);
		Minigames.getInstance().saveConfig();
	}

	public static int getPoints(final OfflinePlayer player){
		if (!Minigames.getInstance().getConfig().isSet("points." + player.getUniqueId())) {
			return 0;
		}

		return Minigames.getInstance().getConfig().getInt("points." + player.getUniqueId());
	}

	public static void addPoints(final OfflinePlayer player, final int points){
		setPoints(player, getPoints(player) + points);
	}

	public static void removePoints(final OfflinePlayer player, final int points){
		setPoints(player, getPoints(player) - points);
	}

	public static class UpdateLeaderboard extends BukkitRunnable {

		@Override
		public void run() {
			final Map<OfflinePlayer, Integer> map = new HashMap<>();

			//Adding all players with their points to a hashmap
			for (final String string : Minigames.getInstance().getConfig().getConfigurationSection("points").getKeys(false)){
				final UUID uuid = UUID.fromString(string);
				final OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
				final int points = Minigames.getInstance().getConfig().getInt("points." + uuid);
				map.put(player, points);
			}

			//Sorting the HashMap by value
			final Map<OfflinePlayer, Integer> sorted = Utils.sortByValue(map);

			final Iterator<Entry<OfflinePlayer, Integer>> iterator = sorted.entrySet().iterator();
			final List<OfflinePlayer> list = new ArrayList<>();
			while (iterator.hasNext()){
				list.add(iterator.next().getKey());
			}

			final OfflinePlayer[] array = list.toArray(new OfflinePlayer[]{});

			final Location[] signLocations = new Location[] {
					new Location(Var.LOBBY_WORLD, 225, 66, 273),
					new Location(Var.LOBBY_WORLD, 225, 66, 274),
					new Location(Var.LOBBY_WORLD, 225, 66, 275),
					new Location(Var.LOBBY_WORLD, 225, 65, 273),
					new Location(Var.LOBBY_WORLD, 225, 65, 274),
					new Location(Var.LOBBY_WORLD, 225, 65, 275),
					new Location(Var.LOBBY_WORLD, 225, 64, 273),
					new Location(Var.LOBBY_WORLD, 225, 64, 274),
					new Location(Var.LOBBY_WORLD, 225, 64, 275),
			};

			for (int i = 0; i < 9; i++) {
				final ChatColor color = ChatColor.WHITE;
				final String nickname = array[i].getName();
				final Sign sign = (Sign) signLocations[i].getBlock().getState();
				sign.setLine(0, ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + (i + 1) + ChatColor.DARK_GRAY + "]");
				sign.setLine(1, color + nickname);
				sign.setLine(2, ChatColor.WHITE + "" + Points.getPoints(array[i]));
				sign.update();
			}

			final int players = Bukkit.getOfflinePlayers().length;
			final int totalPoints = map.values().stream().mapToInt(Integer::intValue).sum();

			final Sign globalStats = (Sign) new Location(Var.LOBBY_WORLD, 221, 65, 280).getBlock().getState();
			globalStats.setLine(0, ChatColor.YELLOW + "Players");
			globalStats.setLine(1, ChatColor.WHITE + "" + players);
			globalStats.setLine(2, ChatColor.YELLOW + "Points");
			globalStats.setLine(3, ChatColor.WHITE + "" + totalPoints);
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

}
