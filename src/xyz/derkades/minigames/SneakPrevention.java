package xyz.derkades.minigames;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.utils.Utils;

/**
 * Runs every 5 ticks
 */
public class SneakPrevention extends BukkitRunnable {

	private static final int MAX_WARNINGS = 10;

	/**
	 * player not in map - Sneak prevention disabled
	 * player in map - Sneak prevention is enabled
	 */
	private static final Map<UUID, Integer> WARNINGS = new HashMap<>();

	SneakPrevention(final Plugin plugin) {
		this.runTaskTimer(plugin, 50, 5);
	}

	@Override
	public void run() {
//		Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).filter(WARNINGS::containsKey)
//			.forEach(u -> WARNINGS.put(u, WARNINGS.get(u) + 1));

		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (!player.isSneaking()) {
				continue;
			}

			final UUID uuid = player.getUniqueId();
			if (WARNINGS.containsKey(uuid)) {
				WARNINGS.put(uuid, WARNINGS.get(uuid) + 1);
				final int remaining = (MAX_WARNINGS - WARNINGS.get(uuid));
				player.sendMessage(Utils.getChatPrefix(ChatColor.RED, 'W') +
						"Sneaking is disabled here. " + remaining + " warnings remaining");
				if (remaining <= 0) {
					player.kickPlayer("Kicked for sneaking");
					WARNINGS.remove(uuid);
				}
			}
		}

	}

	public static void setCanSneak(final Player player, final boolean canSneak) {
		if (canSneak) {
			if (WARNINGS.containsKey(player.getUniqueId())) {
				WARNINGS.remove(player.getUniqueId());
			}
		} else {
			WARNINGS.put(player.getUniqueId(), 0); // Reset warnings
		}
	}

	public static boolean getCanSneak(final Player player) {
		return WARNINGS.containsKey(player.getUniqueId());
	}



}
