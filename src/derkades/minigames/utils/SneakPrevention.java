package derkades.minigames.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import derkades.minigames.Minigames;
import net.md_5.bungee.api.ChatColor;

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
	
	private static final Map<UUID, Consumer<MPlayer>> ON_PUNISH = new HashMap<>();

	public SneakPrevention(final Plugin plugin) {
		runTaskTimer(plugin, 50, 5);
	}

	@Override
	public void run() {
		for (final MPlayer player : Minigames.getOnlinePlayers()) {
			if (!player.bukkit().isSneaking() || !isEnabled(player)) {
				continue;
			}

			final UUID uuid = player.getUniqueId();

			WARNINGS.put(uuid, WARNINGS.get(uuid) + 1);
			final int remaining = MAX_WARNINGS - WARNINGS.get(uuid);

			player.sendTitle(ChatColor.RED + "No Sneaking!", ChatColor.GRAY + "" + remaining + " warnings remaining.");

			if (remaining <= 0) {
				final Consumer<MPlayer> onPunish = ON_PUNISH.get(player.getUniqueId());
				onPunish.accept(player);
				disable(player);
				enable(player, onPunish);
			}
		}

	}
	
	static void enable(final MPlayer player, final Consumer<MPlayer> onPunish) {
		if (isEnabled(player)) {
			return;
		}
		
		WARNINGS.put(player.getUniqueId(), 0);
		ON_PUNISH.put(player.getUniqueId(), onPunish);
	}
	
	static void disable(final MPlayer player) {
		WARNINGS.remove(player.getUniqueId());
		ON_PUNISH.remove(player.getUniqueId());
	}
	
	static boolean isEnabled(final MPlayer player) {
		return WARNINGS.containsKey(player.getUniqueId());
	}

//	public static void setCanSneak(final Player player, final boolean canSneak) {
//		if (canSneak) {
//			if (WARNINGS.containsKey(player.getUniqueId())) {
//				WARNINGS.remove(player.getUniqueId());
//			}
//		} else {
//			WARNINGS.put(player.getUniqueId(), 0); // Reset warnings
//		}
//	}

//	@Deprecated
//	public static boolean getCanSneak(final Player player) {
//		return WARNINGS.containsKey(player.getUniqueId());
//	}

}
