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
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.menu.NameColor;
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

			final World world = Var.LOBBY_LOCATION.getWorld();

			final Sign sign1 = (Sign) new Location(world, 225, 66, 273).getBlock().getState();
			sign1.setLine(0, this.getSignNumberText(1));
			sign1.setLine(1, NameColor.getNameColor(array[0]) + array[0].getName());
			sign1.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[0]));
			sign1.update();

			final Sign sign2 = (Sign) new Location(world, 225, 66, 274).getBlock().getState();
			sign2.setLine(0, this.getSignNumberText(2));
			sign2.setLine(1, NameColor.getNameColor(array[1]) + array[1].getName());
			sign2.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[1]));
			sign2.update();

			final Sign sign3 = (Sign) new Location(world, 225, 66, 275).getBlock().getState();
			sign3.setLine(0, this.getSignNumberText(3));
			sign3.setLine(1, NameColor.getNameColor(array[2]) + array[2].getName());
			sign3.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[2]));
			sign3.update();

			final Sign sign4 = (Sign) new Location(world, 225, 65, 273).getBlock().getState();
			sign4.setLine(0, this.getSignNumberText(4));
			sign4.setLine(1, NameColor.getNameColor(array[3]) + array[3].getName());
			sign4.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[3]));
			sign4.update();

			final Sign sign5 = (Sign) new Location(world, 225, 65, 274).getBlock().getState();
			sign5.setLine(0, this.getSignNumberText(5));
			sign5.setLine(1, NameColor.getNameColor(array[4]) + array[4].getName());
			sign5.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[4]));
			sign5.update();

			final Sign sign6 = (Sign) new Location(world, 225, 65, 275).getBlock().getState();
			sign6.setLine(0, this.getSignNumberText(6));
			sign6.setLine(1, NameColor.getNameColor(array[5]) + array[5].getName());
			sign6.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[5]));
			sign6.update();

			final Sign sign7 = (Sign) new Location(world, 225, 64, 273).getBlock().getState();
			sign7.setLine(0, this.getSignNumberText(7));
			sign7.setLine(1, NameColor.getNameColor(array[6]) + array[6].getName());
			sign7.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[6]));
			sign7.update();

			final Sign sign8 = (Sign) new Location(world, 225, 64, 274).getBlock().getState();
			sign8.setLine(0, this.getSignNumberText(8));
			sign8.setLine(1, NameColor.getNameColor(array[7]) + array[7].getName());
			sign8.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[7]));
			sign8.update();

			final Sign sign9 = (Sign) new Location(world, 225, 64, 275).getBlock().getState();
			sign9.setLine(0, this.getSignNumberText(9));
			sign9.setLine(1, NameColor.getNameColor(array[8]) + array[8].getName());
			sign9.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[8]));
			sign9.update();

			final int players = Bukkit.getOfflinePlayers().length;
			int totalPoints = 0;
			for (final int points : map.values()) {
				totalPoints += points;
			}

			final Sign globalStats = (Sign) new Location(world, 222, 68, 259).getBlock().getState();
			globalStats.setLine(0, ChatColor.GOLD + "Total players");
			globalStats.setLine(1, ChatColor.WHITE + "" + players);
			globalStats.setLine(2, ChatColor.GOLD + "Total points");
			globalStats.setLine(3, ChatColor.WHITE + "" + totalPoints);
			globalStats.update();
		}

		private String getSignNumberText(final int number){
			return ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + number + ChatColor.DARK_GRAY + "]";
		}

	}


}
