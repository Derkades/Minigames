package xyz.derkades.minigames.menu;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.Minigames;

public class NameColor {
	
	public static void setNameColor(Player player, String color){
		Minigames.getInstance().getConfig().set("color." + player.getUniqueId(), 
				color.replace(ChatColor.COLOR_CHAR + "", "&") + " hi");
		Minigames.getInstance().saveConfig();
	}
	
	public static String getNameColor(OfflinePlayer player){
		try {
			String s = Minigames.getInstance().getConfig()
					.getString("color." + player.getUniqueId())
					.replace("&", ChatColor.COLOR_CHAR + "")
					.replace(" hi", "");
			return s;
		} catch (NullPointerException e){
			return ChatColor.GRAY.toString();
		}
	}

}