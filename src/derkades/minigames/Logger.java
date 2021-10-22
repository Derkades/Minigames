package derkades.minigames;

import derkades.minigames.utils.Scheduler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Logger {

	static boolean debugMode = Minigames.getInstance().getConfig().getBoolean("debug_mode");

	public static void warning(final String message, final Object... args) {
		if (!Bukkit.isPrimaryThread()) {
			Scheduler.run(() -> {
				warning(message, args);
			});
			return;
		}

		final String formatted = String.format(message, args);
		Minigames.getInstance().getLogger().warning(formatted);
		if (debugMode) {
			broadcast(ChatColor.RED + "Warning: " + ChatColor.GRAY + formatted);
		}
	}

	public static void info(final String message, final Object... args) {
		if (!Bukkit.isPrimaryThread()) {
			Scheduler.run(() -> {
				info(message, args);
			});
			return;
		}

		final String formatted = String.format(message, args);
		Minigames.getInstance().getLogger().info(formatted);
		if (debugMode) {
			broadcast(ChatColor.BLUE + "Info: " + ChatColor.GRAY + formatted);
		}
	}

	public static void debug(final String message, final Object... args) {
		if (!Bukkit.isPrimaryThread()) {
			Scheduler.run(() -> {
				debug(message, args);
			});
			return;
		}

		if (debugMode) {
			final String formatted = String.format(message, args);
			Minigames.getInstance().getLogger().info(formatted);
			broadcast(ChatColor.GRAY + "Debug: " + ChatColor.DARK_GRAY + formatted);
		}
	}

	private static void broadcast(final String message) {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(message);
		}
	}

	public static boolean debugModeEnabled() {
		return debugMode;
	}

}
