package xyz.derkades.minigames.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Winners {

	public static Set<UUID> fromPointsMap(final Map<UUID, Integer> points){
		if (points == null || points.isEmpty()) {
			return Collections.emptySet();
		}

		final int maxPoints = Collections.max(points.values());

		if (maxPoints == 0) {
			return Collections.emptySet();
		}

		return points.entrySet().stream().filter(e -> e.getValue() == maxPoints)
				.map(Entry::getKey)
				.collect(Collectors.toSet());
	}

	public static Set<UUID> fromDead(final List<UUID> dead, final List<UUID> all, final boolean multipleWinnersAllowed){
		final Set<UUID> winners = all.stream()
				.filter(p -> !dead.contains(p))
				.collect(Collectors.toSet());

		if (multipleWinnersAllowed) {
			return winners;
		}

		if (winners.size() == 1) {
			return winners;
		} else {
			return Collections.emptySet();
		}
	}

}
