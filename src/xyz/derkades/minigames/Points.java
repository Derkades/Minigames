package xyz.derkades.minigames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.utils.Utils;

public class Points {
	
	public static void setPoints(OfflinePlayer player, int points){
		Main.getInstance().getConfig().set("points." + player.getUniqueId(), points);
		Main.getInstance().saveConfig();
	}
	
	public static int getPoints(OfflinePlayer player){
		if (!Main.getInstance().getConfig().isSet("points." + player.getUniqueId()))
			return 0;
		
		return Main.getInstance().getConfig().getInt("points." + player.getUniqueId());
	}
	
	public static void addPoints(OfflinePlayer player, int points){
		setPoints(player, getPoints(player) + points);
	}
	
	public static void removePoints(OfflinePlayer player, int points){
		setPoints(player, getPoints(player) - points);
	}
	
	public static class UpdateLeaderboard extends BukkitRunnable {

		@Override
		public void run() {
			Map<OfflinePlayer, Integer> map = new HashMap<>();
			
			//Adding all players with their points to a hashmap
			for (String string : Main.getInstance().getConfig().getConfigurationSection("points").getKeys(false)){
				UUID uuid = UUID.fromString(string);
				OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
				int points = Main.getInstance().getConfig().getInt("points." + uuid);
				map.put(player, points);
			}
			
			//Sorting the HashMap by value
			Map<OfflinePlayer, Integer> sorted = Utils.sortByValue(map);
			
			Iterator<Entry<OfflinePlayer, Integer>> iterator = sorted.entrySet().iterator();
			List<OfflinePlayer> list = new ArrayList<OfflinePlayer>();
			while (iterator.hasNext()){
				list.add(iterator.next().getKey());
			}
			OfflinePlayer[] array = list.toArray(new OfflinePlayer[]{});
			
			Sign sign1 = (Sign) new Location(Var.WORLD, 221, 69, 260).getBlock().getState();
			sign1.setLine(0, getSignNumberText(1));
			sign1.setLine(1, NameColor.getNameColor(array[0]) + array[0].getName());
			sign1.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[0]));
			sign1.update();
			
			Sign sign2 = (Sign) new Location(Var.WORLD, 220, 69, 260).getBlock().getState();
			sign2.setLine(0, getSignNumberText(2));
			sign2.setLine(1, NameColor.getNameColor(array[1]) + array[1].getName());
			sign2.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[1]));
			sign2.update();
			
			Sign sign3 = (Sign) new Location(Var.WORLD, 219, 69, 260).getBlock().getState();
			sign3.setLine(0, getSignNumberText(3));
			sign3.setLine(1, NameColor.getNameColor(array[2]) + array[2].getName());
			sign3.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[2]));
			sign3.update();
			
			Sign sign4 = (Sign) new Location(Var.WORLD, 221, 68, 260).getBlock().getState();
			sign4.setLine(0, getSignNumberText(4));
			sign4.setLine(1, NameColor.getNameColor(array[3]) + array[3].getName());
			sign4.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[3]));
			sign4.update();
			
			Sign sign5 = (Sign) new Location(Var.WORLD, 220, 68, 260).getBlock().getState();
			sign5.setLine(0, getSignNumberText(5));
			sign5.setLine(1, NameColor.getNameColor(array[4]) + array[4].getName());
			sign5.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[4]));
			sign5.update();
			
			Sign sign6 = (Sign) new Location(Var.WORLD, 219, 68, 260).getBlock().getState();
			sign6.setLine(0, getSignNumberText(6));
			sign6.setLine(1, NameColor.getNameColor(array[5]) + array[5].getName());
			sign6.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[5]));
			sign6.update();
			
			Sign sign7 = (Sign) new Location(Var.WORLD, 221, 67, 260).getBlock().getState();
			sign7.setLine(0, getSignNumberText(7));
			sign7.setLine(1, NameColor.getNameColor(array[6]) + array[6].getName());
			sign7.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[6]));
			sign7.update();
			
			Sign sign8 = (Sign) new Location(Var.WORLD, 220, 67, 260).getBlock().getState();
			sign8.setLine(0, getSignNumberText(8));
			sign8.setLine(1, NameColor.getNameColor(array[7]) + array[7].getName());
			sign8.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[7]));
			sign8.update();
			
			Sign sign9 = (Sign) new Location(Var.WORLD, 219, 67, 260).getBlock().getState();
			sign9.setLine(0, getSignNumberText(9));
			sign9.setLine(1, NameColor.getNameColor(array[8]) + array[8].getName());
			sign9.setLine(2, ChatColor.RESET + "" + Points.getPoints(array[8]));
			sign9.update();
		}
		
		private String getSignNumberText(int number){
			return ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + number + ChatColor.DARK_GRAY + "]";
		}
		
	}


}
