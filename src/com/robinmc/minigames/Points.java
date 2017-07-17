package com.robinmc.minigames;

import org.bukkit.OfflinePlayer;

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

}
