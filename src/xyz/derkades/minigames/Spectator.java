package xyz.derkades.minigames;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.minigames.utils.Utils;

@Deprecated
public class Spectator {

	public static void dieTo(final Player player, final Location location) {
		player.teleport(location);
		die(player);
	}

	public static void dieUp(final Player player, final int yUp) {
		Utils.teleportUp(player, yUp);
		die(player);
	}

	public static void finishTo(final Player player, final Location location) {
		player.teleport(location);
		finish(player);
	}

	public static void finishUp(final Player player, final int yUp) {
		Utils.teleportUp(player, yUp);
		finish(player);
	}

	public static void die(final Player player) {
		Utils.sendTitle(player, "", ChatColor.RED + "You've died");
		spectator(player);
	}

	public static void finish(final Player player) {
		Utils.sendTitle(player, "", ChatColor.GREEN + "You've finished");
		spectator(player);
	}

	public static void spectator(final Player player) {
		player.setGameMode(GameMode.SPECTATOR);
		SneakPrevention.setCanSneak(player, true);
	}

}
