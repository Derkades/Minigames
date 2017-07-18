package xyz.derkades.minigames;

import java.util.List;

import org.bukkit.OfflinePlayer;

public class VIP {
	
	public static boolean isStaff(OfflinePlayer player){
		final List<String> uuids = Minigames.getInstance().getConfig().getStringList("vip");
		return uuids.contains(player.getUniqueId().toString());
	}
	
	public static void setStaff(OfflinePlayer player, boolean setStaff){
		final List<String> uuids = Minigames.getInstance().getConfig().getStringList("vip");
		if (setStaff){
			uuids.add(player.getUniqueId().toString());
		} else {
			uuids.remove(player.getUniqueId().toString());
		}
		Minigames.getInstance().getConfig().set("vip", uuids);
		Minigames.getInstance().saveConfig();
	}

}
