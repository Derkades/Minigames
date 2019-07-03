package xyz.derkades.minigames.utils;

import java.util.ArrayList;
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

	public static List<Player> getWinnersFromPointsHashmap(final Map<UUID, Integer> points){
		if (points == null || points.isEmpty()) {
			return new ArrayList<>();
		}

		final int maxPoints = Collections.max(points.values());

		if (maxPoints == 0) {
			return new ArrayList<>();
		}

		return points.entrySet().stream().filter(e -> e.getValue() == maxPoints)
				.map(e -> Bukkit.getPlayer(e.getKey())).filter(p -> p != null)
				.collect(Collectors.toList());
	}

	public static int getAliveCountFromDeadList(final List<UUID> dead) {
		return Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).filter(uuid -> !dead.contains(uuid)).toArray().length;
	}

	public static int getAliveAcountFromDeadAndAllList(final List<UUID> dead, final List<UUID> all) {
		int alive = 0;

		for (final Player player : getPlayerListFromUUIDList(all)) {
			if (!dead.contains(player.getUniqueId())) {
				alive++;
			}
		}

		return alive;
	}

	public static List<Player> getPlayerListFromUUIDList(final List<UUID> list){
		return list.stream().map(Bukkit::getPlayer).filter(p -> p != null).collect(Collectors.toList());
	}

	public static List<Player> getWinnersFromDeadAndAllList(final List<UUID> dead, final List<UUID> all, final boolean multipleWinnersAllowed){
		final List<Player> winners = all.stream().map(Bukkit::getPlayer).filter(p -> p != null)
				.filter(p -> !dead.contains(p.getUniqueId())).collect(Collectors.toList());

		if (multipleWinnersAllowed) {
			return winners;
		}

		if (winners.size() == 1) {
			return winners;
		} else {
			return new ArrayList<>();
		}
	}

	public static List<Player> getWinnersFromFinished(final List<UUID> finished, final List<UUID> all) {
		return all.stream().filter(finished::contains).map(Bukkit::getPlayer).filter(p -> p != null).collect(Collectors.toList());
	}

	public static List<Player> getWinnersFromFinished(final List<UUID> finished) {
		return Bukkit.getOnlinePlayers().stream().filter((p) -> finished.contains(p.getUniqueId())).collect(Collectors.toList());
	}

	public static List<Player> getWinnersFromAliveList(final List<UUID> alive, final boolean multipleWinnersAllowed){
		final List<Player> winners = alive.stream().map(Bukkit::getPlayer).filter(p -> p != null).collect(Collectors.toList());

		if (multipleWinnersAllowed) {
			return winners;
		}

		if (winners.size() == 1) {
			return winners;
		} else {
			return new ArrayList<>();
		}
	}

	public static List<UUID> getOnlinePlayersUuidList(){
		return Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList());
	}

	@Deprecated
	public static void playSoundForAllPlayers(final Sound sound, final float pitch) {
		for (final Player player : Bukkit.getOnlinePlayers())
			player.playSound(player.getLocation(), sound, 1, pitch);
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

	public static boolean isIn2dBounds(final Location location, final Location cornerOne, final Location cornerTwo) {
		if (!location.getWorld().equals(cornerOne.getWorld())) {
			return false;
		}

		final int maxX = Math.max(cornerOne.getBlockX(), cornerTwo.getBlockX());
		final int minX = Math.min(cornerOne.getBlockX(), cornerTwo.getBlockX());
		final int maxZ = Math.max(cornerOne.getBlockZ(), cornerTwo.getBlockZ());
		final int minZ = Math.min(cornerOne.getBlockZ(), cornerTwo.getBlockZ());

		final int x = location.getBlockX();
		final int z = location.getBlockZ();

		return x > minX && x < maxX &&
				z > minZ && z < maxZ;
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
			if (!finished.contains(player.getUniqueId())) {
				return false;
			}
		}
		return true;
	}

}
