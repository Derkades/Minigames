package derkades.minigames.menu;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import derkades.minigames.Minigames;
import derkades.minigames.utils.MPlayer;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

public class PointsListMenu extends IconMenu {

	public PointsListMenu(final Player player) {
		super(Minigames.getInstance(), "Points", 3, player);

		int slot = 0;
		for (final MPlayer target : Minigames.getOnlinePlayers()) {
			this.addItem(slot, new ItemBuilder(target.bukkit())
					.name(ChatColor.GOLD + target.getName())
					.lore(ChatColor.GRAY + "Points: " + ChatColor.YELLOW + target.getPoints())
					.create());
			slot++;
		}

		this.addItem(24, new ItemBuilder(Material.PLAYER_HEAD).name(ChatColor.GOLD + "View all players").create());
		final OfflinePlayer mhfQuestion = Bukkit.getOfflinePlayer(UUID.fromString("606e2ff0-ed77-4842-9d6c-e1d3321c7838"));
		this.addItem(25, new ItemBuilder(mhfQuestion).name(ChatColor.GOLD + "Points explanation")
				.lore(ChatColor.GREEN + "" + ChatColor.BOLD + "Winning a game",
						ChatColor.GRAY + "  1-2 online players: +3 points, +3 GladeCoins",
						ChatColor.GRAY + "  3-4 online players: +4 points, +4 GladeCoins",
						ChatColor.GRAY + "  5+ online players: +5 points, +5 GladeCoins",
						ChatColor.RED + "" + ChatColor.BOLD + "Losing a game",
						ChatColor.GRAY + "  +1 point, +0 GladeCoins").create());
		this.addItem(26, Menu.BACK_BUTTON);
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		final Player player = event.getPlayer();
		if (event.getPosition() == 26) {
			new MainMenu(player);
		} else if (event.getPosition() == 24) {
			new PointsLeaderboardMenu(player);
		}
		return false;
	}

}
