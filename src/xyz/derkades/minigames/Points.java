package xyz.derkades.minigames;

import java.util.ArrayList;
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

import com.mineglade.icore.utils.PlayerData;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.utils.Utils;

public class Points {

	public static void setPoints(final OfflinePlayer player, final int points){
		Minigames.getInstance().getConfig().set("points." + player.getUniqueId(), points);
		Minigames.getInstance().saveConfig();
	}

	public static int getPoints(final OfflinePlayer player){
		if (!Minigames.getInstance().getConfig().isSet("points." + player.getUniqueId()))
			return 0;

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
			final List<OfflinePlayer> list = new ArrayList<OfflinePlayer>();
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
				final OfflinePlayer player = array[i];
				final PlayerData icoreData = new PlayerData(player);
				final String nickname = icoreData.getNickName().equals("") ? player.getName() : icoreData.getNickName();
				final ChatColor color = icoreData.getNameColor();
				final Sign sign = (Sign) signLocations[i].getBlock().getState();
				sign.setLine(0, ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + (i + 1) + ChatColor.DARK_GRAY + "]");
				sign.setLine(1, color + nickname);
				sign.setLine(2, ChatColor.WHITE + "" + Points.getPoints(array[i]));
				sign.update();
			}

			final int players = Bukkit.getOfflinePlayers().length;
			int totalPoints = 0;
			for (final int points : map.values()) {
				totalPoints += points;
			}

			final Sign globalStats = (Sign) new Location(Var.LOBBY_WORLD, 222, 68, 259).getBlock().getState();
			globalStats.setLine(0, ChatColor.YELLOW + "Total players");
			globalStats.setLine(1, ChatColor.WHITE + "" + players);
			globalStats.setLine(2, ChatColor.YELLOW + "Total points");
			globalStats.setLine(3, ChatColor.WHITE + "" + totalPoints);
			globalStats.update();
		}

	}


}
