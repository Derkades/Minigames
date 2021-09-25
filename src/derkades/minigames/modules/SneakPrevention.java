package derkades.minigames.modules;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import derkades.minigames.Minigames;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Scheduler;

/**
 * Runs every 5 ticks
 */
public class SneakPrevention extends Module implements Runnable {

	private static final int MAX_WARNINGS = 10;

	/**
	 * player not in map - Sneak prevention disabled
	 * player in map - Sneak prevention is enabled
	 */
	private static final Map<UUID, Integer> WARNINGS = new HashMap<>();

	private static final Map<UUID, Consumer<MPlayer>> ON_PUNISH = new HashMap<>();

	public SneakPrevention() {
		Scheduler.repeat(5, this);
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

//			player.sendTitle(ChatColor.RED + "No Sneaking!", ChatColor.GRAY + "" + remaining + " warnings remaining.");
			player.sendTitle(
					text("No sneaking!", RED),
					text(remaining + " warnings remaining", GRAY)
					);

			if (remaining <= 0) {
				final Consumer<MPlayer> onPunish = ON_PUNISH.get(player.getUniqueId());
				onPunish.accept(player);
				disable(player);
				enable(player, onPunish);
			}
		}

	}

	public static void enable(final MPlayer player, final Consumer<MPlayer> onPunish) {
		if (isEnabled(player)) {
			return;
		}

		WARNINGS.put(player.getUniqueId(), 0);
		ON_PUNISH.put(player.getUniqueId(), onPunish);
	}

	public static void disable(final MPlayer player) {
		WARNINGS.remove(player.getUniqueId());
		ON_PUNISH.remove(player.getUniqueId());
	}

	public static boolean isEnabled(final MPlayer player) {
		return WARNINGS.containsKey(player.getUniqueId());
	}

}
