package derkades.minigames;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Logger {

	static boolean debugMode = true;

	public static void warning(final String message, final Object... arg1) {
		final String formatted = String.format(message, arg1);
		Minigames.getInstance().getLogger().warning(formatted);
		if (debugMode) {
			broadcast(ChatColor.RED + "Warning: " + ChatColor.GRAY + formatted);
		}
	}

	public static void info(final String message, final Object... arg1) {
		final String formatted = String.format(message, arg1);
		Minigames.getInstance().getLogger().info(formatted);
		if (debugMode) {
			broadcast(ChatColor.BLUE + "Info: " + ChatColor.GRAY + formatted);
		}
	}

	public static void debug(final String message, final Object... arg1) {
		if (debugMode) {
			final String formatted = String.format(message, arg1);
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
