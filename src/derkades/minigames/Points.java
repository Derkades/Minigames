package derkades.minigames;

import org.bukkit.OfflinePlayer;

public class Points {

	public static void setPoints(final OfflinePlayer player, final int points){
		Minigames.getInstance().getConfig().set("points." + player.getUniqueId(), points);
		Minigames.getInstance().queueConfigSave();
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

}
