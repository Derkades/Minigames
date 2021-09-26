package derkades.minigames.utils;

import derkades.minigames.Minigames;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Utils {

	@NotNull
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(@NotNull final Map<K, V> map) {
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

	@SuppressWarnings("null")
	public static @NotNull List<@NotNull UUID> getOnlinePlayersUuidList(){
		return Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList());
	}

	@SuppressWarnings("null")
	public static @NotNull Set<@NotNull UUID> getOnlinePlayersUuidSet(){
		return Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toSet());
	}

	@NotNull
	public static <Key> List<Key> getHighestValuesFromHashMap(@NotNull final Map<Key, Integer> map){
		final Integer max = map.entrySet()
	            .stream()
	            .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
	            .get().getValue();

	    return map.entrySet() .stream()
	            .filter(entry -> entry.getValue().equals(max))
	            .map(Map.Entry::getKey)
	            .collect(Collectors.toList());
	}

	// Taken from https://stackoverflow.com/a/11926952
	public static <E> E getWeightedRandom(final Map<E, Double> weights) {
	    E result = null;
	    double bestValue = Double.MAX_VALUE;

	    for (final E element : weights.keySet()) {
	        final double value = -Math.log(ThreadLocalRandom.current().nextDouble()) / weights.get(element);

	        if (value < bestValue) {
	            bestValue = value;
	            result = element;
	        }
	    }

	    return result;
	}

	@SuppressWarnings("null")
	public static void showEveryoneToEveryone() {
		for (final Player player1 : Bukkit.getOnlinePlayers()) {
			for (final Player player2 : Bukkit.getOnlinePlayers()) {
				player1.showPlayer(Minigames.getInstance(), player2);
			}
		}
	}

	@NotNull
	@Deprecated
	public static String getChatPrefix(final ChatColor color, final char c) {
		return ChatColor.DARK_GRAY + "[" + color + c + ChatColor.DARK_GRAY + "]" + ChatColor.DARK_GRAY + " | " + ChatColor.GRAY;
	}

	@SuppressWarnings("null")
	public static boolean allPlayersFinished(@NotNull final Set<@NotNull UUID> finished) {
		for (final @NotNull Player player : Bukkit.getOnlinePlayers()) {
			if (!finished.contains(player.getUniqueId())) {
				return false;
			}
		}
		return true;
	}

	@Nullable
	public static MPlayer getDamagerPlayer(@NotNull final EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			return getDamagerPlayer(((EntityDamageByEntityEvent) event).getDamager());
		} else {
			return null;
		}
	}

	@Nullable
	public static MPlayer getDamagerPlayer(@Nullable final Entity damagerEntity) {
		if (damagerEntity == null) {
			return null;
		}
		switch(damagerEntity.getType()) {
			case PLAYER -> {
				return new MPlayer((Player) damagerEntity);
			}
			case ARROW, SPECTRAL_ARROW, FIREBALL -> {
				final ProjectileSource shooter = ((Projectile) damagerEntity).getShooter();
				if (shooter instanceof Player) {
					return new MPlayer((Player) shooter);
				} else {
					return null;
				}
			}
			default -> {
				return null;
			}
		}
	}

	@Nullable
	public static MPlayer getKiller(@NotNull final PlayerDeathEvent event) {
		final EntityDamageEvent cause = event.getEntity().getLastDamageCause();
		return cause == null ? null : getDamagerPlayer(cause);
	}

}
