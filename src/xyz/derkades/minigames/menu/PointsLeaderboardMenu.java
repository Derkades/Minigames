package xyz.derkades.minigames.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.utils.Utils;

public class PointsLeaderboardMenu extends IconMenu {

	public PointsLeaderboardMenu(final Player player) {
		super(Minigames.getInstance(), "Points leaderboard", 6*9, player);

		this.items.put(53, Menu.BACK_BUTTON);

		final Map<OfflinePlayer, Integer> map = new HashMap<>();

		//Adding all players with their points to a hashmap
		for (final String string : Minigames.getInstance().getConfig().getConfigurationSection("points").getKeys(false)){
			final UUID uuid = UUID.fromString(string);
			final OfflinePlayer player2 = Bukkit.getOfflinePlayer(uuid);
			final int points = Minigames.getInstance().getConfig().getInt("points." + uuid);
			map.put(player2, points);
		}

		final Map<OfflinePlayer, Integer> sorted = Utils.sortByValue(map);

		int slot = 0;
		for (final Map.Entry<OfflinePlayer, Integer> e : sorted.entrySet()) {
			if (slot > 52) {
				return;
			}

			this.items.put(slot, new ItemBuilder(e.getKey()).amount(slot + 1).name(ChatColor.GOLD + e.getKey().getName()).lore(ChatColor.GRAY + "Points: " + ChatColor.YELLOW + e.getValue()).create());
			slot++;
		}
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		final Player player = event.getPlayer();
		if (event.getPosition() == 53) {
			new PointsListMenu(player).open();
		}
		return false;
	}

}
