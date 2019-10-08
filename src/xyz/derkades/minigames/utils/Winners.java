package xyz.derkades.minigames.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Winners {

	public static List<UUID> fromPointsMap(final Map<UUID, Integer> points){
		if (points == null || points.isEmpty())
			return new ArrayList<>();

		final int maxPoints = Collections.max(points.values());

		if (maxPoints == 0)
			return new ArrayList<>();

		return points.entrySet().stream().filter(e -> e.getValue() == maxPoints)
				.map(Entry::getKey)
				.collect(Collectors.toList());
	}

	public static List<Player> getPlayerListFromUUIDList(final List<UUID> list){
		return list.stream().map(Bukkit::getPlayer).filter(p -> p != null).collect(Collectors.toList());
	}

	public static List<UUID> fromDead(final List<UUID> dead, final List<UUID> all, final boolean multipleWinnersAllowed){
		final List<UUID> winners = all.stream()
				.filter(p -> !dead.contains(p))
				.collect(Collectors.toList());

		if (multipleWinnersAllowed)
			return winners;

		if (winners.size() == 1)
			return winners;
		else
			return new ArrayList<>();
	}

//	@Deprecated
//	public static List<Player> getWinnersFromFinished(final List<UUID> finished, final List<UUID> all) {
//		return all.stream().filter(finished::contains).map(Bukkit::getPlayer).filter(p -> p != null).collect(Collectors.toList());
//	}

//	@Deprecated
//	public static List<Player> getWinnersFromFinished(final List<UUID> finished) {
//		return Bukkit.getOnlinePlayers().stream().filter((p) -> finished.contains(p.getUniqueId())).collect(Collectors.toList());
//	}

	public static List<UUID> fromAlive(final List<UUID> alive, final boolean multipleWinnersAllowed){
		if (multipleWinnersAllowed)
			return alive;

		if (alive.size() == 1)
			return alive;
		else
			return new ArrayList<>();
	}

	@Deprecated
	public static int countAliveFromDead(final List<UUID> dead) {
		return Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).filter(uuid -> !dead.contains(uuid)).toArray().length;
	}

	@Deprecated
	public static int countAliveFromDead(final List<UUID> dead, final List<UUID> all) {
		int alive = 0;

		for (final Player player : getPlayerListFromUUIDList(all)) {
			if (!dead.contains(player.getUniqueId())) {
				alive++;
			}
		}

		return alive;
	}

}