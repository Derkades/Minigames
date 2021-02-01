package derkades.minigames.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import derkades.minigames.Minigames;
import derkades.minigames.utils.Scheduler;
import derkades.minigames.utils.Utils;
import derkades.minigames.utils.queue.TaskQueue;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

public class PointsLeaderboardMenu extends IconMenu {
	
	private final int page;

	public PointsLeaderboardMenu(final Player player) {
		this(player, 0);
	}
	
	private PointsLeaderboardMenu(final Player player, final int page) {
		super(Minigames.getInstance(), "Points leaderboard", 4, player);
		
		this.page = page;
		
		this.addItem(31, Menu.BACK_BUTTON);
		
		Scheduler.async(() -> {
			final Map<OfflinePlayer, Integer> sorted = getSortedPointsMap();
			int pos = -1;
			int slot = 0;
			for (final Entry<OfflinePlayer, Integer> e : sorted.entrySet()) {
				if (++pos < page*36) {
					continue;
				}
				
				if (slot > 26) {
					TaskQueue.add(() -> this.addItem(35, Menu.NEXT_BUTTON));
					break;
				}
				
				final int finalPos = pos;
				final int finalSlot = slot++;
				TaskQueue.add(() -> {
					final String playerName = e.getKey().getName();
					final ItemStack item = new ItemBuilder(e.getKey()).amount(finalPos + 1).name(ChatColor.GOLD + playerName).lore(ChatColor.GRAY + "Points: " + ChatColor.YELLOW + e.getValue()).create();
					this.addItem(finalSlot, item);
				});
			}
			
			if (page > 0) {
				TaskQueue.add(() -> this.addItem(27, Menu.PREV_BUTTON));
			}
		});
	}
	
	private Map<OfflinePlayer, Integer> getSortedPointsMap() {
		final Map<OfflinePlayer, Integer> map = new HashMap<>();

		//Adding all players with their points to a hashmap
		for (final String string : Minigames.getInstance().getConfig().getConfigurationSection("points").getKeys(false)){
			final UUID uuid = UUID.fromString(string);
			final OfflinePlayer player2 = Bukkit.getOfflinePlayer(uuid);
			final int points = Minigames.getInstance().getConfig().getInt("points." + uuid);
			map.put(player2, points);
		}

		return Utils.sortByValue(map);
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		final Player player = event.getPlayer();
		if (event.getPosition() == 31) {
			new PointsListMenu(player);
		} else if (event.getPosition() == 27) {
			if (this.page > 0) {
				new PointsLeaderboardMenu(player, this.page - 1);
			}
		} else if (event.getPosition() == 35) {
			new PointsLeaderboardMenu(player, this.page + 1);
		}
		return false;
	}

}
