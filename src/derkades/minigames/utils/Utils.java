package derkades.minigames.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import derkades.minigames.Minigames;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import xyz.derkades.derkutils.Random;

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
	
	public static Set<UUID> getOnlinePlayersUuidSet(){
		return Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toSet());
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

	public static void showEveryoneToEveryone() {
		for (final Player player1 : Bukkit.getOnlinePlayers()) {
			for (final Player player2 : Bukkit.getOnlinePlayers()) {
				player1.showPlayer(Minigames.getInstance(), player2);
			}
		}
	}

	public static String getChatPrefix(final ChatColor color, final char c) {
		return ChatColor.DARK_GRAY + "[" + color + c + ChatColor.DARK_GRAY + "]" + ChatColor.DARK_GRAY + " | " + ChatColor.GRAY;
	}

	public static ComponentBuilder getComponentBuilderWithPrefix(final ChatColor prefixColor, final char prefixChar) {
		return new ComponentBuilder("").appendLegacy(Utils.getChatPrefix(prefixColor, prefixChar));
	}

	public static boolean allPlayersFinished(final Set<UUID> finished) {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (!finished.contains(player.getUniqueId())) {
				return false;
			}
		}
		return true;
	}

}
