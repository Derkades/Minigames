package xyz.derkades.minigames.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.Minigames;

public class Utils {

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map) {
	    return map.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
	              .collect(Collectors.toMap(
	                Map.Entry::getKey,
	                Map.Entry::getValue,
	                (e1, e2) -> e1,
	                LinkedHashMap::new
	              ));
	}

	public static List<UUID> getOnlinePlayersUuidList(){
		return Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList());
	}

	@Deprecated
	public static void playSoundForAllPlayers(final Sound sound, final float pitch) {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			player.playSound(player.getLocation(), sound, 1, pitch);
		}
	}

	public static <Key> List<Key> getHighestValuesFromHashMap(final Map<Key, Integer> map){
		final Integer max = map.entrySet()
	            .stream()
	            .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
	            .get().getValue();

	    return map.entrySet() .stream()
	            .filter(entry -> entry.getValue() == max)
	            .map(Map.Entry::getKey)
	            .collect(Collectors.toList());
	}

	// Taken from https://stackoverflow.com/a/11926952
	public static <E> E getWeightedRandom(final Map<E, Double> weights) {
	    E result = null;
	    double bestValue = Double.MAX_VALUE;

	    for (final E element : weights.keySet()) {
	        final double value = -Math.log(Random.getRandomDouble()) / weights.get(element);

	        if (value < bestValue) {
	            bestValue = value;
	            result = element;
	        }
	    }

	    return result;
	}

	@Deprecated
	public static void hideForEveryoneElse(final Player player) {
		Bukkit.getOnlinePlayers().forEach((player2) -> player2.hidePlayer(Minigames.getInstance(), player));
	}

	public static void showEveryoneToEveryone() {
		for (final Player player1 : Bukkit.getOnlinePlayers()) {
			for (final Player player2 : Bukkit.getOnlinePlayers()) {
				player1.showPlayer(Minigames.getInstance(), player2);
			}
		}
	}

	public static void delayedTeleport(final Location location, final Player... players) {
		int delay = 0;

		for (final Player player : players) {
			Scheduler.delay(delay, () -> player.teleport(location));
			delay +=2;
		}
	}

	public static void delayedTeleport(final Location location, final Consumer<Player> runnable, final Player... players) {
		int delay = 0;

		for (final Player player : players) {
			Scheduler.delay(delay, () -> {
				player.teleport(location);
				runnable.accept(player);
			});
			delay +=2;
		}
	}

	public static String getChatPrefix(final ChatColor color, final char c) {
		return ChatColor.DARK_GRAY + "[" + color + c + ChatColor.DARK_GRAY + "]" + ChatColor.DARK_GRAY + " | " + ChatColor.GRAY;
	}

	public static ComponentBuilder getComponentBuilderWithPrefix(final ChatColor prefixColor, final char prefixChar) {
		return new ComponentBuilder("").appendLegacy(Utils.getChatPrefix(prefixColor, prefixChar));
	}

	public static void setMetadata(final Player player, final String key, final Object value) {
		player.removeMetadata("minigames_" + key, Minigames.getInstance());
		player.setMetadata("minigames_" + key, new FixedMetadataValue(Minigames.getInstance(), value));
	}

	public static MetadataValue getMetadata(final Player player, final String key) {
		return player.getMetadata("minigames_" + key).get(0);
	}

	public static boolean allPlayersFinished(final List<UUID> finished) {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (!finished.contains(player.getUniqueId()))
				return false;
		}
		return true;
	}

}
