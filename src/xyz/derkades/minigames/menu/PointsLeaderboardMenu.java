package xyz.derkades.minigames.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.utils.Scheduler;
import xyz.derkades.minigames.utils.Utils;
import xyz.derkades.minigames.utils.queue.TaskQueue;

public class PointsLeaderboardMenu extends IconMenu {

	public PointsLeaderboardMenu(final Player player) {
		super(Minigames.getInstance(), "Points leaderboard", 6, player);

		this.addItem(53, Menu.BACK_BUTTON);
		
		this.addItem(0, new ItemBuilder(Material.LIGHT_GRAY_DYE).name("Loading...").create());
		
		Scheduler.async(() -> {
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
			for (final Entry<OfflinePlayer, Integer> e : sorted.entrySet()) {
				if (slot > 52) {
					return;
				}
				
				final int finalSlot = slot++;
				TaskQueue.add(() -> {
					final String playerName = e.getKey().getName();
					final ItemStack item = new ItemBuilder(e.getKey()).amount(finalSlot + 1).name(ChatColor.GOLD + playerName).lore(ChatColor.GRAY + "Points: " + ChatColor.YELLOW + e.getValue()).create();
					this.addItem(finalSlot, item);
				});
			}
		});
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		final Player player = event.getPlayer();
		if (event.getPosition() == 53) {
			new PointsListMenu(player);
		}
		return false;
	}

}
